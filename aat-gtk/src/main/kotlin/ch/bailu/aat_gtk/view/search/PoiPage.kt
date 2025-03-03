package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxStatic
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Spinner
import ch.bailu.gtk.type.Str

class PoiPage(appContext: AppContext, controller: UiControllerInterface, app: Application, window: ApplicationWindow) {

    private var poiHandle: Obj = ObjNull

    private val showMapButton = Button().apply {
        setLabel(Res.str().p_map())
        onClicked { controller.showMap() }
    }

    private val loadButton = Button.newWithLabelButton(Res.str().load()).apply {
        onClicked {
            spinner.start()
            poiView.loadList()
        }
    }

    private val spinner = Spinner()
    private val countLabel = Label(Str.NULL)

    private val headerBar = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        append(showMapButton)
        append(loadButton)
        append(spinner)
        append(countLabel)
    }

    init {
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_ONDISK) { args: Array<out String> ->
            val handle = poiHandle
            if (BroadcastData.has(args, poiView.poiApi.resultFile.pathName)) {
                poiHandle = appContext.services.getCacheService().getObject(poiView.poiApi.resultFile.pathName)
                handle.free()
                spinner.stop()
            }
        }

        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE) { args: Array<out String> ->
            val handle = poiHandle
            if (BroadcastData.has(args, handle.getID()) && handle is ObjGpxStatic) {
                countLabel.setLabel(handle.getGpxList().pointList.size().toString())
            }
        }
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
