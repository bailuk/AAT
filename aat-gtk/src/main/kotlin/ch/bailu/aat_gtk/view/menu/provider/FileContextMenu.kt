package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.ellipsizeStart
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AFile
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import ch.bailu.gtk.adw.MessageDialog
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class FileContextMenu(private val solid: SolidCustomOverlayList, private val solidMock: SolidMockLocationFile): MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSection(Res.str().file_overlay(), Menu().apply {
                appendItem(MenuHelper.createCustomItem(solid.key))
            })

            appendSection(Str.NULL, Menu().apply {
                append(Res.str().edit_load_menu(), Strings.actionFileEdit)
                append(Res.str().file_mock(), Strings.actionFileMock)
                append(Res.str().file_rename(), Strings.actionFileRename)
                append(Res.str().file_delete(), Strings.actionFileDelete)
            })
        }
    }

    private var file: Foc = FocName(solid.key)
    private var removedFromList = file

    fun setFile(file: Foc) {
        this.file = file
        removedFromList = file
    }

    override fun createActions(app: Application) {

        MenuHelper.setAction(app, Strings.actionFileMock) {
            solidMock.setValue(file.path)
        }
        MenuHelper.setAction(app, Strings.actionFileRename) {
            rename(app)
        }

        MenuHelper.setAction(app, Strings.actionFileDelete) {
            delete(app)
        }
    }

    private fun delete(app: Application) {

        if (file.canWrite()) {
            val dialog = MessageDialog(app.activeWindow, Res.str().file_delete_ask(), file.pathName)

            dialog.addResponse(Strings.idCancel, Res.str().cancel())
            dialog.addResponse(Strings.idOk, Res.str().ok())

            dialog.onResponse {
                val res = it.toString()
                if (Strings.idOk == res) {
                    file.rm()
                    FileAction.rescanDirectory(GtkAppContext, file)
                }
            }
            dialog.present()
        } else {
            AFile.logErrorReadOnly(file)
        }
    }


    private fun rename(app: Application) {
        if (file.canWrite() && file.hasParent()) {
            val directory = file.parent()
            val dialog = MessageDialog(app.activeWindow, Res.str().file_rename(), file.name)
            val entry = Entry()
            dialog.extraChild = entry
            dialog.addResponse(Strings.idCancel, Res.str().cancel())
            dialog.addResponse(Strings.idOk, Res.str().ok())
            dialog.onResponse {
                val res = it.toString()
                if (Strings.idOk == res) {
                    val source = directory.child(file.name)
                    val target = directory.child(entry.asEditable().text.toString())

                    FileAction.rename(GtkAppContext, source, target)
                }
            }
            dialog.present()
        }
    }

     override fun createCustomWidgets(): Array<CustomWidget> {
         val labels: ArrayList<Label> = ArrayList()
         val checkButtons: ArrayList<CheckButton> = ArrayList()

         return arrayOf(
            CustomWidget(
                ListBox().apply {
                    selectionMode = 1

                    solid.enabledArray.forEachIndexed { index, _ ->
                        val layout = Box(Orientation.HORIZONTAL, 5)
                        val check = CheckButton()
                        val label = Label(Str.NULL)

                        label.xalign = 0f
                        labels.add(label)

                        check.onToggled {
                            solid.setEnabled(index, check.active)
                        }
                        checkButtons.add(check)
                        layout.append(check)
                        layout.append(label)
                        append(layout)
                    }

                    onRowActivated {
                        val foundAt = indexOf(file)

                        if (foundAt == -1) {
                            removedFromList = solid[it.index].valueAsFile
                            solid[it.index].setValueFromFile(file)
                        }
                        else if (foundAt != it.index) {
                            val tmp = solid[it.index].valueAsFile
                            solid[it.index].setValueFromFile(file)
                            solid[foundAt].setValueFromFile(tmp)
                        } else {
                            solid[it.index].setValueFromFile(removedFromList)
                        }

                        updateLabels(labels)
                    }
                }, solid.key
            ) {
                updateLabels(labels)
                updateCheckButtons(checkButtons)
            }
        )
    }

    private fun indexOf(file: Foc): Int {
        for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
            if (solid[i].valueAsFile == file) {
                return i
            }
        }
        return -1
    }

    private fun updateCheckButtons(checkButtons: List<CheckButton>) {
        checkButtons.forEachIndexed { index, it ->
            it.active = solid[index].isEnabled
        }

    }

    private fun updateLabels(labels: List<Label>) {
        labels.forEachIndexed { index, it ->
            it.setText(solid[index].valueAsString.ellipsizeStart(30))
        }
    }
}
