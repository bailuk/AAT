package ch.bailu.aat.views.image

import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.service.cache.icons.ObjSVGAsset
import ch.bailu.aat_lib.service.icons.IconMapService

class SVGAssetView(private val scontext: ServiceContext, rid: Int) : ImageObjectView(scontext, rid) {
    private val size: Int

    init {
        scaleType = ScaleType.CENTER
        size = AndroidAppDensity(scontext.getContext()).toPixelInt(IconMapService.BIG_ICON_SIZE)
    }

    fun setImageObject(key: Int, value: String) {
        scontext.insideContext { setImageObject(scontext.getIconMapService().toAssetPath(key, value)) }
    }

    fun setImageObject(gpxPointNode: GpxPointNode) {
        scontext.insideContext { setImageObject(scontext.getIconMapService().toAssetPath(gpxPointNode)) }
    }

    private fun setImageObject(name: String?) {
        if (name is String) {
            val id = ObjSVGAsset.toID(name, size)
            setImageObject(id, ObjSVGAsset.Factory(name, size))
        } else {
            setImageObject()
        }
    }
}
