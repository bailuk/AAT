package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.lib.FileDialog
import ch.bailu.aat_gtk.view.menu.PopupButton
import ch.bailu.aat_gtk.view.menu.model.FixedLabelItem
import ch.bailu.aat_gtk.view.menu.model.Menu
import ch.bailu.aat_gtk.view.util.GtkLabel
import ch.bailu.aat_gtk.view.util.escapeUnderscore
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str

class SolidDirectorySelectorView(private val solid: SolidFile, actionHelper: ActionHelper, window: Window) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)

    private val label = GtkLabel()

    private val hbox = Box(Orientation.HORIZONTAL, 5)
    private val entry = Entry()
    private val menu = PopupButton(actionHelper, Menu().also { it ->
        val selection = solid.buildSelection(ArrayList())
        for (s in selection) {
            it.add(FixedLabelItem(s.escapeUnderscore()) {
                solid.setValue(s)
            })
        }

        it.add(FixedLabelItem("…") {
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
        })
    })


    init {
        label.text = solid.label
        label.xalign = 0f

        entry.hexpand = GTK.TRUE
        menu.setIcon("menu", 12)
        layout.append(label)
        layout.append(hbox)

        hbox.append(entry)
        hbox.append(menu.overlay)

        entry.buffer.setText(Str(solid.toString()), solid.toString().length)
        solid.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            entry.buffer.setText(Str(solid.toString()), solid.toString().length)
        }
    }
}