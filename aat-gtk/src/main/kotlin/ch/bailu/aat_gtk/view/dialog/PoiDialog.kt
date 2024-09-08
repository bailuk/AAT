package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.search.PoiView
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Window
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

object PoiDialog {
    private var window: Window? = null

    fun show(uiController: UiControllerInterface, app: Application) {
        if (window == null) {
            window = Window().apply {
                val poiView = PoiView(uiController, app, this)

                application = app
                content = Box(Orientation.VERTICAL, 0).apply {
                    append(HeaderBar().apply {
                        titleWidget = (WindowTitle(GtkAppConfig.appName, Res.str().p_mapsforge_poi()))

                        packStart(Box(Orientation.HORIZONTAL, Layout.margin).apply {
                            append(Button.newWithLabelButton(Res.str().load()).apply {
                                onClicked { poiView.loadList() }
                            })
                        })
                        packEnd(Box(Orientation.HORIZONTAL,Layout.margin ).apply {
                            append(Button.newWithLabelButton(ToDo.translate("Close")).apply {
                                onClicked { close() }
                            })
                        })
                    })
                    append(poiView.layout)
                }

                setDefaultSize(Layout.windowWidth + Layout.windowWidth / 2 , Layout.windowHeight + Layout.windowHeight / 2)

                onDestroy {
                    poiView.onDestroy()
                    window?.disconnectSignals()
                    window = null
                }
            }
        }
        window?.show()
    }
}
