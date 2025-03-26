package ch.bailu.aat.map.mapsforge

import android.content.Context
import android.view.View
import ch.bailu.aat.map.MapDensity
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.lifecycle.LifeCycleInterface
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.map.layer.LayerWrapper
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Dimension
import org.mapsforge.core.model.MapPosition
import org.mapsforge.core.util.LatLongUtils
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.Layer
import org.mapsforge.map.model.MapViewPosition

open class MapsForgeViewBase(
    appContext: AppContext,
    context: Context,
    key: String,
    d: MapDensity
) : MapView(context), MapViewInterface, LifeCycleInterface, OnPreferencesChanged {

    private var pendingFrameBounding: BoundingBox? = null
    private val mcontext: MapsForgeContext
    private val services: ServicesInterface
    private val storage: Storage
    private var areServicesUp = false
    private var isVisible = false
    val layers = ArrayList<MapLayerInterface>(10)
    private var areLayersAttached = false

    init {
        setBackgroundColor(model.displayModel.backgroundColor)
        model.displayModel.setFixedTileSize(d.tileSize)
        services = appContext.services
        mcontext = MapsForgeContext(appContext, this, key, d)
        add(mcontext)
        storage = Storage(context)
        mapScaleBar.isVisible = false
        setBuiltInZoomControls(false)
    }

    override fun onChange() {
        // Disable MapView.onChange to fix a speed bug in MapsForge
    }

    override fun add(layer: MapLayerInterface) {
        val wrapper: Layer = if (layer is Layer) {
            layer
        } else {
            LayerWrapper(services, mcontext, layer)
        }
        addLayer(wrapper)
        layers.add(layer)
        if (areLayersAttached) layer.onAttached()
    }

    override fun getMContext(): MapContext {
        return mcontext
    }

    fun toView(): View {
        return this
    }

    override fun reDownloadTiles() {}
    override fun getMapViewPosition(): MapViewPosition {
        return model.mapViewPosition
    }

    override fun zoomOut() {
        model.mapViewPosition.zoomOut()
    }

    override fun zoomIn() {
        model.mapViewPosition.zoomIn()
    }

    override fun requestRedraw() {
        if (areLayersAttached) layerManager.redrawLayers()
    }

    override fun frameBounding(boundingBox: BoundingBoxE6) {
        frameBounding(boundingBox.toBoundingBox())
    }

    private fun frameBounding(bounding: BoundingBox) {
        val dimension = model.mapViewDimension.dimension
        if (dimension == null) {
            pendingFrameBounding = bounding
        } else {
            val zoom = zoomForBounds(bounding, dimension)
            val position = MapPosition(bounding.centerPoint, zoom)
            model.mapViewPosition.mapPosition = position
            pendingFrameBounding = null
        }
    }

    private fun zoomForBounds(bounding: BoundingBox, dimension: Dimension): Byte {
        var zoom: Byte = if (bounding.minLatitude == 0.0 && bounding.minLongitude == 0.0 && bounding.maxLatitude == 0.0 && bounding.maxLongitude == 0.0) {
                0
            } else {
                LatLongUtils.zoomForBounds(
                    dimension,
                    bounding,
                    model.displayModel.tileSize
                )
            }
        zoom = minOf(zoom.toInt(), model.mapViewPosition.zoomLevelMax.toInt()).toByte()
        zoom = maxOf(zoom.toInt(), model.mapViewPosition.zoomLevelMin.toInt()).toByte()
        return zoom
    }

    public override fun onSizeChanged(nw: Int, nh: Int, ow: Int, oh: Int) {
        super.onSizeChanged(nw, nh, ow, oh)
        if (pendingFrameBounding != null) {
            frameBounding(pendingFrameBounding!!)
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        for (l in layers) l.onPreferencesChanged(storage, key)
    }

    public override fun onLayout(c: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (c) {
            for (layer in layers) layer.onLayout(c, l, t, r, b)
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isVisible = visibility == VISIBLE
        attachDetachLayers()
    }

    public override fun onDetachedFromWindow() {
        isVisible = false
        attachDetachLayers()
        super.onDetachedFromWindow()
    }

    override fun setVisibility(v: Int) {
        super.setVisibility(v)
        isVisible = v == VISIBLE
        attachDetachLayers()
    }

    override fun onResumeWithService() {
        storage.register(this)
        areServicesUp = true
        attachDetachLayers()
    }

    override fun onPauseWithService() {
        storage.unregister(this)
        areServicesUp = false
        attachDetachLayers()
    }

    override fun onDestroy() {
        detachLayers()
        destroyAll()

        /* FIXME: this is a workaround to a bug:
         * Sometimes the LayerManager thread is still running after calling destroyAll().
         * This happens when MapView was never attached to window.
         * Same problem with the Animator thread of MapViewPosition. */
        layerManager.finish()
        getMapViewPosition().destroy()
    }

    private fun attachDetachLayers() {
        if (isVisible && areServicesUp) {
            attachLayers()
        } else {
            detachLayers()
        }
    }

    protected fun attachLayers() {
        if (!areLayersAttached) {
            for (l in layers) l.onAttached()
            areLayersAttached = true
            requestRedraw()
        }
    }

    private fun detachLayers() {
        if (areLayersAttached) {
            for (l in layers) l.onDetached()
            areLayersAttached = false
        }
    }
}
