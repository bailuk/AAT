package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.util.GtkLabel
import ch.bailu.aat_gtk.view.util.truncate
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidDirectorySelectorView(private val solid: SolidFile, window: Window) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 2)


    private val label = GtkLabel()

    private val selector = Box(Orientation.HORIZONTAL, 5)
    private val button = Button()
    private val combo = ComboBoxText()
    private val dropDown = MenuButton()


    init {
        label.text = solid.label
        label.xalign = 0f

        layout.append(label)
        layout.append(selector)
        selector.append(button)
        selector.append(dropDown)

        button.label = Str(solid.toString().truncate())
        solid.register(this)

        val selection = solid.buildSelection(ArrayList())

        for (index in 0 until selection.size) {
            combo.insertText(index, Str(selection[index]))
        }
        //combo.active = solid.index

        button.onClicked {
            val dialog = FileChooserDialog(label.label, window,FileChooserAction.SELECT_FOLDER,null)
            dialog.addButton(Str("OK"), ResponseType.OK)

            dialog.onResponse {
                if (it == ResponseType.OK) {
                    val file = FileChooser(dialog.cast()).file
                    val path = file.path

                    solid.setValueFromString(path.toString())
                    path.destroy()
                    ch.bailu.gtk.gobject.Object(file.cast()).unref()
                    dialog.close()
                }
            }
            dialog.show()
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            button.label = Str(solid.toString().truncate())
        }
    }
}