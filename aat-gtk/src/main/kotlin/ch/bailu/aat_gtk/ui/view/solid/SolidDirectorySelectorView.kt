package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.view.Label
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidDirectorySelectorView(private val solid: SolidFile, window: Window) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 2)

    private val label = Label()

    private val button = Button()


    init {
        label.text = solid.label
        layout.append(label)
        layout.append(button)

        button.label = Str(solid.toString())
        solid.register(this)

        button.onClicked {
            var dialog = FileChooserDialog(label.label, window,FileChooserAction.SELECT_FOLDER,null)
            dialog.addButton(Str("OK"), ResponseType.OK)

            dialog.onResponse {
                if (it == ResponseType.OK) {
                    var file = FileChooser(dialog.cast()).file
                    var path = file.path

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
            button.label = Str(solid.toString())
        }
    }
}