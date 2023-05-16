package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.adw.PreferencesPage
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.type.Str

object AdwPreferencesDialog {
    private var window: PreferencesWindow? = null

    fun show(uiController: UiController, app: Application) {
        if (window == null) {
            window = PreferencesWindow().apply {

                canNavigateBack = true

                add(PreferencesPage().apply {
                    vexpand = true
                    setTitle("Test")
                    setName("test")

                })

                add(PreferencesPage().apply {
                    vexpand = true
                    setTitle("Test2")
                    setName("test2")
                    add(PreferencesGroup().apply {
                        description = Str("Description")
                        title = Str("Title")
                        add(ActionRow().apply {
                            title = Str("Hallo")
                            useUnderline = true
                        })
                    })

                })

                setDefaultSize(Layout.windowWidth, Layout.windowHeight)

                onDestroy {
                    System.err.println("TODO: Clean up here")
                    window?.disconnectSignals()
                    window = null
                }
            }
        }
        window?.show()
    }
}
