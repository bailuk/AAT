package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.search.PoiStackView
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Window
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation

object PoiDialog {
    private var window: Window? = null

    fun show(uiController: UiController, app: Application) {
        if (window == null) {
            window = Window().apply {
                val stackView = PoiStackView(uiController, app, this)

                content = Box(Orientation.VERTICAL, 0).apply {
                    append(HeaderBar().apply {
                        titleWidget =
                            (WindowTitle(GtkAppConfig.shortName, Res.str().p_mapsforge_poi()))

                    })
                    append(stackView.layout)
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
}
