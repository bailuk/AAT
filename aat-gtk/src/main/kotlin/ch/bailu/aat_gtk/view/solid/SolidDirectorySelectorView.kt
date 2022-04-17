package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.lib.FileDialog
import ch.bailu.aat_gtk.lib.menu.MenuFacade
import ch.bailu.aat_gtk.view.util.GtkLabel
import ch.bailu.aat_gtk.view.util.setText
import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_gtk.view.util.truncate
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidDirectorySelectorView(private val solid: SolidFile, app: Application, window: Window) :
    OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)

    private val label = GtkLabel()

    private val hbox = Box(Orientation.HORIZONTAL, 5)
    private val entry = Entry()

    init {
        label.text = solid.label
        label.xalign = 0f

        entry.hexpand = GTK.TRUE

        layout.append(label)
        layout.append(hbox)

        hbox.append(entry)

        hbox.append(MenuButton().apply {
            menuModel = createMenuModel(app, window)

            PopoverMenu(popover.cast()).apply {
                onShow {
                    addChild(createCustomWidget(), Str(solid.key))
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

    private fun createMenuModel(app: Application, window: Window): Menu {
        return MenuFacade(app).apply {
            build()
                .custom(solid.key)
                .label(ToDo.translate("File dialog...")) {
                    FileDialog()
                        .label(solid.label)
                        .selectFolder()
                        .path(solid.valueAsString)
                        .response {
                            if (it.isNotEmpty()) {
                                solid.setValueFromString(it)
                            }
                        }
                        .show(window)
                }
        }.model
    }


    private fun createCustomWidget(): Widget {
        return ListBox().apply {
            solid.buildSelection(ArrayList()).apply {
                onRowActivated {
                    solid.setValue(this[it.index])
                }
            }.forEach {
                val label = Label(Str.NULL)
                label.setText(it.truncate())
                label.xalign = 0f
                label.margin(3)
                append(label)
            }
        }
    }


    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            Editable(entry.cast()).text = Str(solid.valueAsString)
        }
    }
}
