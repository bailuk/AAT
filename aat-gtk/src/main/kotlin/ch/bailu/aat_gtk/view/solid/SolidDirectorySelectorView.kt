package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkRefs
import ch.bailu.aat_gtk.view.menu.provider.SolidFileSelectorMenu
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidDirectorySelectorView(private val solid: SolidFile, app: Application, window: Window) :
    OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)

    private val label = Label(Str.NULL)

    private val hbox = Box(Orientation.HORIZONTAL, 5)
    private val entry = Entry()

    private val fileSelectorMenu = SolidFileSelectorMenu(solid, window)

    init {
        GtkRefs.text(label, solid.label)
        label.xalign = 0f

        entry.hexpand = GTK.TRUE

        layout.append(label)
        layout.append(hbox)

        hbox.append(entry)

        hbox.append(MenuButton().apply {
            menuModel = fileSelectorMenu.createMenu().create(app)

            PopoverMenu(popover.cast()).apply {
                onShow {
                    fileSelectorMenu.createCustomWidgets().forEach {
                        addChild(it.widget, Str(it.id))
                    }
                }
            }
        })

        entry.overwriteMode = 0
        Editable(entry.cast()).apply {
            text = Str(solid.valueAsString)

            onChanged {
                AppLog.d(this, text.toString())
            }
        }

        solid.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            Editable(entry.cast()).text = Str(solid.valueAsString)
        }
    }
}
