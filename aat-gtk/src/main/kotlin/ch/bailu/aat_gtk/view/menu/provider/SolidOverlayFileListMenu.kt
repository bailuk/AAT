package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.util.setText
import ch.bailu.aat_gtk.view.util.truncate
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.foc.Foc
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidOverlayFileListMenu(private val solid: SolidOverlayFileList, private val file: Foc): MenuProvider {

    private val labels: ArrayList<Label> = ArrayList()
    private var replaced = file

    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder().custom(solid.key)
    }

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

                        check.active = GTK.IS(it)
                        check.onToggled {
                            solid.setEnabled(index, GTK.IS(check.active))
                        }
                        layout.append(check)
                        layout.append(label)
                        append(layout)
                    }

                    updateLabels()
                    onRowActivated {
                        if (!haveAsOverlay(file)) {
                            replaced = solid[it.index].valueAsFile
                            solid[it.index].setValueFromFile(file)
                        } else if (file == solid[it.index].valueAsFile) {
                            solid[it.index].setValueFromFile(replaced)
                        }
                        updateLabels()
                    }
                }, solid.key
            )
        )
    }

    private fun haveAsOverlay(file: Foc): Boolean {
        for (i in 0 until SolidOverlayFileList.MAX_OVERLAYS) {
            if (solid[i].valueAsFile == file) {
                return true
            }
        }
        return false
    }

    private fun updateLabels() {
        labels.forEachIndexed { index, it ->
            it.setText(solid[index].valueAsString.truncate())
        }
    }
}
