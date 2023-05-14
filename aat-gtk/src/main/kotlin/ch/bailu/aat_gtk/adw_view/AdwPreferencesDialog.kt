package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.gtk.adw.PreferencesPage
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Application

object AdwPreferencesDialog {
    private var window: PreferencesWindow? = null

    fun show(uiController: UiController, app: Application) {
        if (window == null) {
            window = PreferencesWindow().apply {
                add(PreferencesPage().apply {
                    setTitle("Test")
                    setName("test")
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
