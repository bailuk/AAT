package ch.bailu.aat_lib.search.poi

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.file.xml.writer.WayWriter
import ch.bailu.aat_lib.file.xml.writer.WayWriterOsmTags
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.preferences.map.SolidPoiOverlay
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.poi.storage.ExactMatchPoiCategoryFilter
import org.mapsforge.poi.storage.PoiCategory
import org.mapsforge.poi.storage.PoiCategoryFilter
import org.mapsforge.poi.storage.PoiPersistenceManager
import org.mapsforge.poi.storage.PointOfInterest
import java.io.IOException

abstract class PoiApi(context: AppContext) : OsmApiConfiguration() {
    private val overlay = SolidPoiOverlay(context.dataDirectory)
    private var task = BackgroundTask.NULL

    override val resultFile: Foc
        get() = overlay.getValueAsFile()

    companion object {
        private const val LIMIT = 10000
    }

    override val fileExtension = AppDirectory.GPX_EXTENSION

    override val apiName: String
        get() = overlay.getLabel()

    override fun getUrl(query: String, bounding: BoundingBoxE6): String {
        return ""
    }

    override val urlStart: String
        get() = ""
    override val baseDirectory: Foc
        get() = overlay.directory

    override fun getUrlPreview(query: String, bounding: BoundingBoxE6): String {
        return ""
    }

    override fun startTask(appContext: AppContext, boundingBoxE6: BoundingBoxE6) {
        val categories = selectedCategories
        val poiDatabase = SolidPoiDatabase(appContext.mapDirectory, appContext).getValueAsString()
        appContext.services.insideContext {
            task.stopProcessing()
            task = PoiToGpxTask(
                resultFile,
                boundingBoxE6.toBoundingBox(),
                categories,
                poiDatabase
            )
            appContext.services.getBackgroundService().process(task)
        }
        overlay.setEnabled(true)
    }

    protected abstract val selectedCategories: ArrayList<PoiCategory>

    private class PoiToGpxTask(result: Foc, private val bounding: BoundingBox, private val categories: ArrayList<PoiCategory>, private val poiDatabase: String) : FileTask(result) {
        override fun bgOnProcess(appContext: AppContext): Long {
            val persistenceManager = appContext.getPoiPersistenceManager(poiDatabase)
            try {
                queryPois(persistenceManager, bounding)
            } catch (e: Exception) {
                AppLog.e(this, e) // TODO friendly message
            }
            persistenceManager.close()
            appContext.broadcaster.broadcast(
                AppBroadcaster.FILE_CHANGED_ONDISK,
                getFile().toString(), poiDatabase
            )
            return 100
        }

        @Throws(IOException::class)
        private fun queryPois(persistenceManager: PoiPersistenceManager, box: BoundingBox) {
            val poiCollection = searchPoi(persistenceManager, box)

            if (poiCollection is Collection<PointOfInterest>) {
                if (getFile().exists()) getFile().remove()
                writeGpxFile(poiCollection)
            }
        }

        private fun searchPoi(persistenceManager: PoiPersistenceManager, box: BoundingBox): Collection<PointOfInterest>? {
            val categoryFilter: PoiCategoryFilter = ExactMatchPoiCategoryFilter()

            for (category in categories) categoryFilter.addCategory(category)
            return persistenceManager.findInRect(box, categoryFilter, null, box.centerPoint, LIMIT, false)
        }

        @Throws(IOException::class)
        private fun writeGpxFile(pois: Collection<PointOfInterest>) {
            val writer: WayWriter = WayWriterOsmTags(getFile())
            writer.writeHeader(System.currentTimeMillis())
            for (poi in pois) {
                writer.writeTrackPoint(GpxPointPoi(poi))
            }
            writer.writeFooter()
            writer.close()
        }
    }
}
