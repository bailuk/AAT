package ch.bailu.aat_lib.search.poi

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.preferences.map.SolidPoiOverlay
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.aat_lib.xml.writer.WayWriter
import ch.bailu.aat_lib.xml.writer.WayWriterOsmTags
import ch.bailu.foc.Foc
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.poi.storage.ExactMatchPoiCategoryFilter
import org.mapsforge.poi.storage.PoiCategory
import org.mapsforge.poi.storage.PoiCategoryFilter
import org.mapsforge.poi.storage.PoiPersistenceManager
import org.mapsforge.poi.storage.PointOfInterest
import java.io.IOException

abstract class PoiApi(context: AppContext, private val bounding: BoundingBoxE6) : OsmApiConfiguration() {
    private val poiOverlay: SolidPoiOverlay
    private var task = BackgroundTask.NULL

    init {
        poiOverlay = SolidPoiOverlay(context.dataDirectory)
    }

    override val exception: Exception?
        get() = task.exception

    override val resultFile: Foc
        get() = poiOverlay.valueAsFile

    companion object {
        private const val LIMIT = 10000
    }

    override val fileExtension = AppDirectory.GPX_EXTENSION

    override val apiName: String
        get() = poiOverlay.label

    override fun getUrl(query: String): String {
        return ""
    }

    override val urlStart: String
        get() = ""
    override val baseDirectory: Foc
        get() = poiOverlay.directory

    override fun getUrlPreview(query: String): String {
        return ""
    }

    override fun startTask(appContext: AppContext) {
        val categories = selectedCategories
        val poiDatabase = SolidPoiDatabase(appContext.mapDirectory, appContext).valueAsString
        appContext.services.insideContext {
            task.stopProcessing()
            task = PoiToGpxTask(
                resultFile,
                bounding.toBoundingBox(),
                categories,
                poiDatabase
            )
            appContext.services.backgroundService.process(task)
        }
        poiOverlay.isEnabled = true
    }

    protected abstract val selectedCategories: ArrayList<PoiCategory>

    private class PoiToGpxTask(result: Foc, private val bounding: BoundingBox, private val categories: ArrayList<PoiCategory>, private val poiDatabase: String) : FileTask(result) {
        override fun bgOnProcess(sc: AppContext): Long {
            val persistenceManager = sc.getPoiPersistenceManager(poiDatabase)
            try {
                queryPois(persistenceManager, bounding)
            } catch (e: Exception) {
                exception = e
            }
            persistenceManager.close()
            sc.broadcaster.broadcast(
                AppBroadcaster.FILE_CHANGED_ONDISK,
                file.toString(), poiDatabase
            )
            return 100
        }

        @Throws(IOException::class)
        private fun queryPois(persistenceManager: PoiPersistenceManager, box: BoundingBox) {
            val poiCollection = searchPoi(persistenceManager, box)

            if (poiCollection is Collection<PointOfInterest>) {
                if (file.exists()) file.remove()
                writeGpxFile(poiCollection)
            }
        }

        private fun searchPoi(persistenceManager: PoiPersistenceManager, box: BoundingBox): Collection<PointOfInterest>? {
            val categoryFilter: PoiCategoryFilter = ExactMatchPoiCategoryFilter()

            for (category in categories) categoryFilter.addCategory(category)
            return persistenceManager.findInRect(box, categoryFilter, null, box.centerPoint, LIMIT)
        }

        @Throws(IOException::class)
        private fun writeGpxFile(pois: Collection<PointOfInterest>) {
            val writer: WayWriter = WayWriterOsmTags(file)
            writer.writeHeader(System.currentTimeMillis())
            for (poi in pois) {
                writer.writeTrackPoint(GpxPointPoi(poi))
            }
            writer.writeFooter()
            writer.close()
        }
    }
}
