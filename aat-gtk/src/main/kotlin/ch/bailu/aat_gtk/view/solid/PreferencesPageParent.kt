package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.adw.PreferencesPage
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

open class PreferencesPageParent(title: String, name: String) {
    val page = PreferencesPage()

    private var createNewGroup = false
    private var newGroup = PreferencesGroup()

    init {
        page.vexpand = true
        page.setTitle(title)
        page.setName(name)
        page.iconName = Icons.preferencesOtherSymbolic
    }

    fun add(title: String) {
        if (createNewGroup) {
            newGroup = PreferencesGroup()
        }

        val strTitle = Str(title)
        newGroup.title = strTitle
        strTitle.destroy()

        page.add(newGroup)
        createNewGroup = true
    }


    fun add(view: Widget) {
        view.margin(Layout.margin)
        newGroup.add(view)
    }
}
