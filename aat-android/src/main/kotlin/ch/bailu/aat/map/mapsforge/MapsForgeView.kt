package ch.bailu.aat.map.mapsforge

import android.content.Context
import android.graphics.Canvas
import ch.bailu.aat.map.MapDensity
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.map.layer.MapPositionLayer
import ch.bailu.aat_lib.map.tile.MapsForgeTileLayerStackConfigured
import org.mapsforge.map.model.common.Observer

class MapsForgeView(
    context: Context,
    appContext: AppContext,
    dispatcher: DispatcherInterface,
    key: String
) : MapsForgeViewBase(appContext, context, key, MapDensity(context)) {

    private val stack = MapsForgeTileLayerStackConfigured.All(this, appContext)
    private val pos = MapPositionLayer(getMContext(), Storage(context), dispatcher)
    private val services = appContext.services
    private val foreground = MapsForgeForeground(appContext, this, getMContext(), MapDensity(context), layers)

    init {
        add(stack, stack)
        add(pos)

        isClickable = true
        model.mapViewPosition.addObserver(object : Observer {
            private var center = model.mapViewPosition.center
            override fun onChange() {
                val newCenter = model.mapViewPosition.center
                if (newCenter != null && newCenter != center) {
                    center = newCenter
                    pos.onMapCenterChanged(center)
                }
            }
        })
    }

    override fun reDownloadTiles() {
        stack.reDownloadTiles()
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        foreground.dispatchDraw(services, canvas)
    }
}
