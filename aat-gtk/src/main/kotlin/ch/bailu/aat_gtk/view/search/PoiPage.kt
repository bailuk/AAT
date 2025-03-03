package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class PoiPage(controller: UiControllerInterface, app: Application, window: ApplicationWindow) {

    private val showMapButton = Button().apply {
        setLabel(Res.str().p_map())
        onClicked {
            controller.showMap()
        }
    }

    private val loadButton = Button.newWithLabelButton(Res.str().load()).apply {
        onClicked { poiView.loadList() }
    }

    private val headerBar = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        append(showMapButton)
        append(loadButton)
    }

    private val poiView = PoiView(controller, app, window)

    val layout = Box(Orientation.VERTICAL, 0).apply {
        margin(Layout.margin)
        append(headerBar)
        append(Clamp().apply {
            maximumSize = Layout.stackWidth
            child = poiView.layout
        })
        hexpand = false
    }
}
