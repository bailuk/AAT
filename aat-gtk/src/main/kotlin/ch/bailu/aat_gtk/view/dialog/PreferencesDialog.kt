package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.view.solid.ActivityPreferencesPage
import ch.bailu.aat_gtk.view.solid.GeneralPreferencesPage
import ch.bailu.aat_gtk.view.solid.MapPreferencesPage
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.gtk.adw.PreferencesDialog
import ch.bailu.gtk.gtk.Application

object PreferencesDialog {
    private var window: PreferencesDialog? = null

    fun show(app: Application, appContext: AppContext) {
        if (window == null) {
            window = PreferencesDialog().apply {
                add(GeneralPreferencesPage(app, app.activeWindow, appContext).page)
                add(MapPreferencesPage(appContext, app, app.activeWindow).page)
                add(ActivityPreferencesPage(appContext.storage).page)

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
        window?.setVisiblePageName("map")
    }
}
