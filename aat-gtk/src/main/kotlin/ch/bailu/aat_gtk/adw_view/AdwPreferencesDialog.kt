package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.view.solid.ActivityPreferencesPage
import ch.bailu.aat_gtk.view.solid.GeneralPreferencesPage
import ch.bailu.aat_gtk.view.solid.MapPreferencesPage
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Application

object AdwPreferencesDialog {
    private var window: PreferencesWindow? = null

    fun show(app: Application) {
        if (window == null) {
            window = PreferencesWindow().apply {

                canNavigateBack = true

                add(GeneralPreferencesPage(GtkAppContext.storage, app, this).page)
                add(MapPreferencesPage(GtkAppContext.storage, app, this).page)

                val presetCount = SolidPresetCount(GtkAppContext.storage)

                for(i in 0 until  presetCount.value) {
                    add(ActivityPreferencesPage(GtkAppContext.storage, i).page)
                }

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

    fun showMap(app: Application) {
        show(app)
        val window = this.window
        if (window is PreferencesWindow) {
            window.setVisiblePageName("map")
        }

    }
}
