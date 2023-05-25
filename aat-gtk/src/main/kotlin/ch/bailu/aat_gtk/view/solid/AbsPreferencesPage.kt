package ch.bailu.aat_gtk.view.solid

import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.adw.PreferencesPage
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

open class AbsPreferencesPage(title: String, name: String) {
    val page = PreferencesPage()



    private var createNewGroup = false
    private var newGroup = PreferencesGroup()

    init {
        page.vexpand = true
        page.setTitle(title)
        page.setName(name)
        page.setIconName("preferences-other-symbolic")
    }

    fun add(title: String) {
        if (createNewGroup) {
            newGroup = PreferencesGroup()
        }

        newGroup.title = Str(title)
        newGroup.description = Str(title)
        page.add(newGroup)
        createNewGroup = true
    }


    fun add(view: Widget) {
        newGroup.add(view)
    }
}
