package ch.bailu.aat_lib.service.cache.gpx

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.file.FileType
import ch.bailu.aat_lib.file.json.GpxListReaderJson
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointLinkedNode
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.linked_list.Node
import ch.bailu.aat_lib.preferences.SolidAutopause
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause
import ch.bailu.aat_lib.preferences.presets.SolidPreset.Companion.getPresetFromFile
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.OnObject
import ch.bailu.aat_lib.service.elevation.Dem3Status
import ch.bailu.aat_lib.service.elevation.ElevationProvider
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.aat_lib.file.xml.parser.gpx.GpxListReaderXml
import ch.bailu.foc.Foc

class ObjGpxStatic(id: String, appContext: AppContext) : ObjGpx(id), ElevationUpdaterClient {
    private var gpxList = GpxList.NULL_TRACK
    private var readyAndLoaded = false
    private val file: Foc

    init {
        appContext.services.getCacheService().addToBroadcaster(this)
        file = appContext.toFoc(id)
    }

    override fun onInsert(appContext: AppContext) {
        reload(appContext)
    }

    override fun onRemove(appContext: AppContext) {
        appContext.services.insideContext {
            appContext.services.getElevationService().cancelElevationUpdates(
                this@ObjGpxStatic
            )
        }
    }

    override fun getFile(): Foc {
        return file
    }

    private fun reload(appContext: AppContext) {
        appContext.services.getBackgroundService().process(FileLoader(file))
    }

    override fun isReadyAndLoaded(): Boolean {
        return readyAndLoaded
    }

    override fun getSize(): Long {
        return gpxList.pointList.size() *
                (GpxPoint.SIZE_IN_BYTES +
                        GpxPointLinkedNode.SIZE_IN_BYTES +
                        Node.SIZE_IN_BYTES)
    }

    private fun setGpxList(list: GpxList) {
        readyAndLoaded = true
        gpxList = list
    }

    override fun getGpxList(): GpxList {
        return gpxList
    }

    class Factory : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjGpxStatic(id, appContext)
        }
    }

    override fun onDownloaded(id: String, url: String, appContext: AppContext) {
        if (id == getID()) {
            reload(appContext)
        }
    }

    override fun onChanged(id: String, appContext: AppContext) {}

    private fun getSrtmTileCoordinates(): List<Dem3Coordinates> {
        val f = SrtmTileCollector()
        f.walkTrack(gpxList)

        val result = ArrayList<Dem3Coordinates>()
        for (i in 0 until f.coordinates.size()) {
            val item = f.coordinates.getValueAt(i)

            if (item is Dem3Coordinates)
                result.add(item)
        }
        return result
    }


    override fun updateFromSrtmTile(appContext: AppContext, srtm: Dem3Tile) {
        ListUpdater(srtm).walkTrack(gpxList)

        appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, toString())
    }

    private class ListUpdater(private val tile: Dem3Tile) : GpxListWalker() {
        override fun doList(l: GpxList): Boolean {
            return tile.status == Dem3Status.VALID
        }

        override fun doSegment(segment: GpxSegmentNode): Boolean {
            return true
        }

        override fun doMarker(marker: GpxSegmentNode): Boolean {
            return true
        }

        override fun doPoint(point: GpxPointNode) {
            if (point.getAltitude() == ElevationProvider.NULL_ALTITUDE.toDouble()) {
                val coordinates = Dem3Coordinates(point.getLatitudeE6(), point.getLongitudeE6())
                if (tile.hashCode() == coordinates.hashCode()) {
                    point.setAltitude(
                        tile.getElevation(
                            point.getLatitudeE6(),
                            point.getLongitudeE6()
                        ).toDouble()
                    )
                }
            }
        }
    }

    private class SrtmTileCollector : GpxListWalker() {
        val coordinates: IndexedMap<String, Dem3Coordinates> = IndexedMap()

        override fun doList(l: GpxList): Boolean {
            return true
        }

        override fun doSegment(segment: GpxSegmentNode): Boolean {
            return true
        }

        override fun doMarker(marker: GpxSegmentNode): Boolean {
            return true
        }

        override fun doPoint(point: GpxPointNode) {
            if (point.getAltitude() == ElevationProvider.NULL_ALTITUDE.toDouble()) {
                val c = Dem3Coordinates(point)
                coordinates.put(c.toString(), c)
            }
        }
    }

    private class FileLoader(file: Foc) : FileTask(file) {
        override fun bgOnProcess(appContext: AppContext): Long {
            var size = 0L

            object : OnObject(appContext, getID(), ObjGpxStatic::class.java) {
                override fun run(handle: Obj) {
                    val owner = handle as ObjGpxStatic

                    size = load(appContext, owner)

                    appContext.services.getElevationService().requestElevationUpdates(
                        owner,
                        owner.getSrtmTileCoordinates()
                    )

                    appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, getID())
                }
            }
            return size
        }

        private fun load(appContext: AppContext, handle: ObjGpxStatic): Long {
            try {
                val fileType = FileType(getFile())
                if (fileType.isJSON) {
                    return loadJSON(handle)
                } else if (fileType.isXML) {
                    return loadXML(appContext, handle)
                }
            } catch (e: Exception) {
                handle.setException(e)
            }
            return 0L
        }

        private fun loadJSON(handle: ObjGpxStatic): Long {
            var size = 0L
            val reader = GpxListReaderJson(getFile())
            handle.setException(reader.exception)

            if (canContinue()) {
                handle.setGpxList(reader.gpxList)
                size = handle.getSize()
            }
            return size
        }

        private fun loadXML(appContext: AppContext, handle: ObjGpxStatic): Long {
            var size = 0L

            val reader = GpxListReaderXml(
                threadControl,
                getFile(),
                getAutoPause(appContext, getPresetFromFile(getFile()))
            )

            handle.setException(reader.exception)

            if (canContinue()) {
                handle.setGpxList(reader.gpxList)
                size = handle.getSize()
            }

            return size
        }

        private fun getAutoPause(appContext: AppContext, preset: Int): AutoPause {
            val spause: SolidAutopause = SolidPostprocessedAutopause(appContext.storage, preset)
            return AutoPause.Time(
                spause.triggerSpeed,
                spause.triggerLevelMillis
            )
        }
    }
}
