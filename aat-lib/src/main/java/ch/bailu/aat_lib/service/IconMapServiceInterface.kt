package ch.bailu.aat_lib.service

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.service.cache.ObjImageInterface

interface IconMapServiceInterface {
    fun getIconSVG(point: GpxPointInterface, icon_size: Int): ObjImageInterface?
    fun toAssetPath(key: Int, value: String): String?
    fun toAssetPath(gpxPointNode: GpxPointNode): String?
}
