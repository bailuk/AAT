package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.map.SolidLegend
import ch.bailu.aat_lib.service.ServicesInterface

abstract class Factory {
    abstract fun legend(slegend: SolidLegend): GpxLayer
    abstract fun layer(mcontext: MapContext, services: ServicesInterface): GpxLayer

    companion object {
        @JvmStatic
        operator fun get(type: GpxType): Factory {
            return if (type === GpxType.WAY) WAY else if (type === GpxType.ROUTE) ROUTE else TRACK
        }

        private val WAY: Factory = object : Factory() {
            override fun legend(slegend: SolidLegend): GpxLayer {
                return slegend.createWayLegendLayer()
            }

            override fun layer(mcontext: MapContext, services: ServicesInterface): GpxLayer {
                return WayLayer(mcontext, services)
            }
        }
        private val ROUTE: Factory = object : Factory() {
            override fun legend(slegend: SolidLegend): GpxLayer {
                return slegend.createRouteLegendLayer()
            }

            override fun layer(mcontext: MapContext, services: ServicesInterface): GpxLayer {
                return RouteLayer(mcontext)
            }
        }
        val TRACK: Factory = object : Factory() {
            override fun legend(slegend: SolidLegend): GpxLayer {
                return slegend.createTrackLegendLayer()
            }

            override fun layer(mcontext: MapContext, services: ServicesInterface): GpxLayer {
                return TrackLayer(mcontext)
            }
        }
    }
}