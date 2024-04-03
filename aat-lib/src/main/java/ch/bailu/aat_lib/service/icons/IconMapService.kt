package ch.bailu.aat_lib.service.icons

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys.Companion.toIndex
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract
import ch.bailu.aat_lib.util.WithStatusText
import ch.bailu.foc.FocFactory
import java.io.IOException

class IconMapService(sc: ServicesInterface, focFactory: FocFactory) : VirtualService(),
    WithStatusText, IconMapServiceInterface {
    private val map: IconMap
    private val cache: IconCache

    init {
        cache = IconCache(sc)
        map = IconMap()
        try {
            val mapFile = focFactory.toFoc(SVG_MAP_FILE)
            IconMapParser(mapFile, map)
        } catch (e: IOException) {
            e(this, e)
        }
    }

    override fun getIconSVG(point: GpxPointInterface, iconSize: Int): ObjImageAbstract? {
        val attr = point.getAttributes()
        val path = toAssetPath(attr)
        return cache.getIcon(path, iconSize)
    }

    override fun toAssetPath(key: Int, value: String): String? {
        val icon: IconMap.Icon? = map[key, value]
        return icon?.svg
    }

    private fun toAssetPath(attr: GpxAttributes): String? {
        val icon = getIconEntry(attr)
        return icon?.svg
    }

    private fun getIconEntry(attr: GpxAttributes): IconMap.Icon? {
        for (i in 0 until attr.size()) {
            val icon = map[attr.getKeyAt(i), attr.getAt(i)]
            if (icon != null) return icon
        }
        return getIconEntryNominatimType(attr)
    }

    private fun getIconEntryNominatimType(attr: GpxAttributes): IconMap.Icon? {
        if (attr.hasKey(N_KEY_KEY) && attr.hasKey(N_KEY_VALUE)) {
            val key = toIndex(attr[N_KEY_KEY])
            val value = attr[N_KEY_VALUE]
            return map[key, value]
        }
        return null
    }

    fun close() {
        cache.close()
    }

    override fun appendStatusText(builder: StringBuilder) {
        map.appendStatusText(builder)
        cache.appendStatusText(builder)
    }

    override fun toAssetPath(gpxPointNode: GpxPointNode): String? {
        return toAssetPath(gpxPointNode.getAttributes())
    }

    companion object {
        const val BIG_ICON_SIZE = 48f
        private val N_KEY_KEY = toIndex("class")
        private val N_KEY_VALUE = toIndex("type")
        const val SVG_SUFFIX = ".svg"
        const val SVG_DIRECTORY = "icons/"
        private const val SVG_MAP_FILE = SVG_DIRECTORY + "iconmap.txt"
    }
}
