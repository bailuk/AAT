package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.view.solid.ActivityPreferencesPage
import ch.bailu.aat_gtk.view.solid.GeneralPreferencesPage
import ch.bailu.aat_gtk.view.solid.MapPreferencesPage
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.gtk.adw.PreferencesDialog
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Application

object PreferencesDialog {
    private var window: PreferencesDialog? = null

    fun show(app: Application, appContext: AppContext) {
        if (window == null) {
            window = PreferencesDialog().apply {

                add(GeneralPreferencesPage(app, app.activeWindow, appContext).page)
                add(MapPreferencesPage(appContext, app, app.activeWindow).page)

                val presetCount = SolidPresetCount(appContext.storage)

                for(i in 0 until  presetCount.value) {
                    add(ActivityPreferencesPage(appContext.storage, i).page)
                }

                onDestroy {
                    window?.disconnectSignals()
                    window = null
                }
            }
        }
        window?.present(app.activeWindow)
    }

    fun showMap(app: Application, appContext: AppContext) {
        show(app, appContext)
        val window = this.window
        if (window is PreferencesWindow) {
            window.setVisiblePageName("map")
        }
    }
}
