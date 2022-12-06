package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.extensions.ellipsizeStart
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidOverlaySelectorMenu(private val solid: SolidOverlayFileList): MenuProvider {
    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem(solid.key))
        }
    }

    private var file: Foc = FocName(solid.key)
    private var removedFromList = file
    private val labels: ArrayList<Label> = ArrayList()

    fun setFile(file: Foc) {
        this.file = file
        removedFromList = file
        updateLabels()
    }

    override fun createActions(app: Application) {}

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    selectionMode = 1

                    solid.enabledArray.forEachIndexed { index, it ->
                        val layout = Box(Orientation.HORIZONTAL, 5)
                        val check = CheckButton()
                        val label = Label(Str.NULL)

                        label.xalign = 0f
                        labels.add(label)

                        check.active = it
                        check.onToggled {
                            solid.setEnabled(index, check.active)
                        }
                        layout.append(check)
                        layout.append(label)
                        append(layout)
                    }

                    updateLabels()
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

                        updateLabels()
                    }
                }, solid.key
            )
        )
    }

    private fun indexOf(file: Foc): Int {
        for (i in 0 until SolidOverlayFileList.MAX_OVERLAYS) {
            if (solid[i].valueAsFile == file) {
                return i
            }
        }
        return -1
    }

    private fun updateLabels() {
        labels.forEachIndexed { index, it ->
            it.setText(solid[index].valueAsString.ellipsizeStart(30))
        }
    }
}
