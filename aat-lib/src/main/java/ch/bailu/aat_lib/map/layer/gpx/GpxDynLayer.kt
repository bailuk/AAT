package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxInformationCache
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidLegend
import ch.bailu.aat_lib.preferences.map.SolidLayerType
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point

class GpxDynLayer(
    storage: StorageInterface,
    private val mcontext: MapContext,
    private val services: ServicesInterface
) : MapLayerInterface, TargetInterface {
    private val infoCache = GpxInformationCache()
    private var gpxOverlay: GpxLayer? = null
    private var legendOverlay: GpxLayer? = null
    private val slegend: SolidLegend = SolidLegend(storage, mcontext.getSolidKey())
    private val solidLayerType = SolidLayerType(storage)
    private var forceReconfigure = true

    constructor(
        storage: StorageInterface, mc: MapContext, services: ServicesInterface,
        dispatcher: DispatcherInterface, iid: Int
    ) : this(storage, mc, services) {
        dispatcher.addTarget(this, iid)
    }

    override fun drawInside(mcontext: MapContext) {
        gpxOverlay?.drawInside(mcontext)
        legendOverlay?.drawInside(mcontext)
    }

    override fun drawForeground(mcontext: MapContext) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    private var type = GpxType.NONE

    init {
        createLegendOverlay()
        createGpxOverlay(0)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        infoCache[iid] = info
        if (forceReconfigure || type !== toType(info)) {
            forceReconfigure = false
            type = toType(info)
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
        val type = toType(infoCache.info)
        gpxOverlay = Factory[type].layer(mcontext, services, solidLayerType, iid)
    }

    private fun createLegendOverlay() {
        val type = toType(infoCache.info)
        legendOverlay = Factory[type].legend(slegend)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onAttached() {}
    override fun onDetached() {}

    companion object {
        private fun toType(i: GpxInformation?): GpxType {
            return if (i != null && i.getGpxList() != null) {
                i.getGpxList().getDelta().getType()
            } else GpxType.NONE
        }
    }
}
