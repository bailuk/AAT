package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_gtk.view.menu.controller.FileContextMenuController
import ch.bailu.aat_gtk.view.menu.controller.FileMenuController
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlay.Companion.MAX_OVERLAYS
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.extensions.ellipsizeStart
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class FileContextMenu(private val appContext: AppContext, private val display: Display): MenuProviderInterface {
    private val solidCustomOverlayList = SolidOverlayList.createCustomOverlayList(appContext)
    private var fileMenuController: FileMenuController? = null
    private var fileContextMenuController: FileContextMenuController? = null
    var file: Foc = FocName.FOC_NULL
        set(value) {
            this.fileMenuController?.file = value
            this.fileContextMenuController?.file = value
            removedFromList = value
            field = value
        }

    private var removedFromList = FocName.FOC_NULL

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSection(Res.str().file_overlay(), Menu().apply {
                appendItem(MenuHelper.createCustomItem(solidCustomOverlayList.getKey()))
            })

            appendSection(Str.NULL, Menu().apply {
                append(Res.str().edit_load_menu(), MenuHelper.toAppAction(Strings.ACTION_FILE_EDIT))
                append(Res.str().file_mock(), MenuHelper.toAppAction(Strings.ACTION_FILE_MOCK))
                append(Res.str().file_reload(), MenuHelper.toAppAction(Strings.ACTION_FILE_RELOAD))

            })

            appendSection(Str.NULL, Menu().apply {
                val directories = AppDirectory.getGpxDirectories(appContext)

                append(Res.str().clipboard_copy(), MenuHelper.toAppAction(Strings.ACTION_FILE_TO_CLIPBOARD))
                appendSubmenu(Res.str().file_copy(), FileMenu.createSelectActivityFolderMenu("", directories))
                append(Res.str().file_rename(), MenuHelper.toAppAction(Strings.ACTION_FILE_RENAME))
                append(Res.str().file_delete(), MenuHelper.toAppAction(Strings.ACTION_FILE_DELETE))
            })
        }
    }

    override fun createActions(app: Application) {
        fileMenuController = FileMenuController("", app, display, appContext)
        fileContextMenuController = FileContextMenuController(app, appContext)
        this.fileMenuController?.file = file
        this.fileContextMenuController?.file = file
    }

    override fun updateActionValues(app: Application) {}



     override fun createCustomWidgets(): Array<CustomWidget> {
         val labels: ArrayList<Label> = ArrayList()
         val checkButtons: ArrayList<CheckButton> = ArrayList()

         return arrayOf(
            CustomWidget(
                ListBox().apply {
                    selectionMode = 1

                    solidCustomOverlayList.getEnabledArray().forEachIndexed { index, _ ->
                        val layout = Box(Orientation.HORIZONTAL, 5)
                        val check = CheckButton()
                        val label = Label(Str.NULL)

                        label.xalign = 0f
                        labels.add(label)

                        check.onToggled {
                            solidCustomOverlayList.setEnabled(index, check.active)
                        }
                        checkButtons.add(check)
                        layout.append(check)
                        layout.append(label)
                        append(layout)
                    }

                    onRowActivated {
                        val foundAt = indexOf(file)

                        if (foundAt == -1) {
                            removedFromList = solidCustomOverlayList[it.index].getValueAsFile()
                            solidCustomOverlayList[it.index].setValueFromFile(file)
                        }
                        else if (foundAt != it.index) {
                            val tmp = solidCustomOverlayList[it.index].getValueAsFile()
                            solidCustomOverlayList[it.index].setValueFromFile(file)
                            solidCustomOverlayList[foundAt].setValueFromFile(tmp)
                        } else {
                            solidCustomOverlayList[it.index].setValueFromFile(removedFromList)
                        }

                        updateLabels(labels)
                    }
                }, solidCustomOverlayList.getKey()
            ) {
                updateLabels(labels)
                updateCheckButtons(checkButtons)
            }
        )
    }

    private fun indexOf(file: Foc): Int {
        for (i in 0 until MAX_OVERLAYS) {
            if (solidCustomOverlayList[i].getValueAsFile() == file) {
                return i
            }
        }
        return -1
    }

    private fun updateCheckButtons(checkButtons: List<CheckButton>) {
        checkButtons.forEachIndexed { index, it ->
            it.active = solidCustomOverlayList[index].isEnabled()
        }

    }

    private fun updateLabels(labels: List<Label>) {
        labels.forEachIndexed { index, it ->
            it.setText(solidCustomOverlayList[index].getValueAsString().ellipsizeStart(30))
        }
    }
}
