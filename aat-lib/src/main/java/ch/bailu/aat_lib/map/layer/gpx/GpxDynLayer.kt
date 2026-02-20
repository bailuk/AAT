package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.GpxInformationCache
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.map.layer.gpx.legend.GpxLegendLayer
import ch.bailu.aat_lib.map.layer.gpx.legend.NullLegendWalker
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidLayerType
import ch.bailu.aat_lib.preferences.map.SolidLegend
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point

class GpxDynLayer(
    storage: StorageInterface,
    private val mcontext: MapContext,
    private val services: ServicesInterface
) : MapLayerInterface, TargetInterface {
    private val infoCache = GpxInformationCache()
    private var gpxOverlay: GpxLayer = TrackLayer(mcontext)
    private var legendOverlay: GpxLayer = GpxLegendLayer(NullLegendWalker())
    private val slegend: SolidLegend = SolidLegend(storage, mcontext.getSolidKey())
    private val solidLayerType = SolidLayerType(storage)
    private var forceReconfigure = true
    private var type = GpxType.TRACK

    constructor(
        storage: StorageInterface, mc: MapContext, services: ServicesInterface,
        dispatcher: DispatcherInterface, iid: Int
    ) : this(storage, mc, services) {
        dispatcher.addTarget(this, iid)
    }

    override fun drawInside(mcontext: MapContext) {
        gpxOverlay.drawInside(mcontext)
        legendOverlay.drawInside(mcontext)
    }
    override fun drawForeground(mcontext: MapContext) {}

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        infoCache.set(iid, info)
        if (forceReconfigure || type !== info.getType()) {
            forceReconfigure = false
            type = info.getType()
            createGpxOverlay(iid)
            createLegendOverlay()
        }
        infoCache.letUpdate(gpxOverlay)
        infoCache.letUpdate(legendOverlay)
        mcontext.getMapView().requestRedraw()
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (slegend.hasKey(key) || solidLayerType.hasKey(key)) {
            forceReconfigure = true
            infoCache.letUpdate(this)
        }
    }

    private fun createGpxOverlay(iid: Int) {
        val type = infoCache.info.getType()
        gpxOverlay = Factory[type].layer(mcontext, services, solidLayerType, iid)
    }

    private fun createLegendOverlay() {
        val type = infoCache.info.getType()
        legendOverlay = Factory[type].legend(slegend)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onAttached() {
        // If coming back from the preferences dialog
        forceReconfigure = true
        infoCache.letUpdate(this)
    }

    override fun onDetached() {}
}
