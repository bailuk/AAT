package ch.bailu.aat_lib.service.render

import ch.bailu.aat_lib.app.AppGraphicFactory.instance
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Point
import org.mapsforge.core.model.Rotation
import org.mapsforge.core.model.Tile
import org.mapsforge.map.datastore.MapDataStore
import org.mapsforge.map.datastore.MultiMapDataStore
import org.mapsforge.map.layer.Layer
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.layer.queue.JobQueue
import org.mapsforge.map.layer.renderer.DatabaseRenderer
import org.mapsforge.map.layer.renderer.MapWorkerPool
import org.mapsforge.map.layer.renderer.RendererJob
import org.mapsforge.map.model.DisplayModel
import org.mapsforge.map.model.Model
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.reader.header.MapFileException
import org.mapsforge.map.rendertheme.XmlRenderTheme
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture

class Renderer(renderTheme: XmlRenderTheme, cache: TileCache, files: ArrayList<Foc>, userScaleFactor: Float) : Layer() {
    private var mapDataStore: MapDataStore? = null
    private var mapWorkerPool: MapWorkerPool? = null
    private var renderThemeFuture: RenderThemeFuture? = null


    private var jobQueue: JobQueue<RendererJob>? = null


    init {
        // important: call model.mapViewPosition.destroy() to free resources
        val model = Model()

        try {
            displayModel = model.displayModel
            displayModel.userScaleFactor = userScaleFactor
            jobQueue = JobQueue(model.mapViewPosition, model.displayModel)

            renderThemeFuture = createTheme(renderTheme, displayModel)
            mapDataStore = createMapDataStore(files)

            val databaseRenderer = DatabaseRenderer(
                mapDataStore,
                instance(),
                cache,
                null,
                RENDER_LABELS,
                CACHE_LABELS, null
            )

            mapWorkerPool = MapWorkerPool(
                cache,
                jobQueue,
                databaseRenderer,
                this
            )


            mapWorkerPool?.start()
        } catch (e: Exception) {
            destroy()
            throw e
        } finally {
            model.mapViewPosition.destroy()
        }
    }


    @Throws(Exception::class)
    private fun createMapDataStore(files: ArrayList<Foc>): MapDataStore {
        val result: MapDataStore
        val mapFiles = createMapFiles(files)

        result = if (mapFiles.size == 1) {
            mapFiles[0]
        } else {
            createMultiMapDataStore(mapFiles)
        }

        return result
    }


    private fun createMultiMapDataStore(mapFiles: ArrayList<MapFile>): MapDataStore {
        val result = MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL)
        for (mapFile in mapFiles) {
            result.addMapDataStore(mapFile, true, true)
        }
        return result
    }


    @Throws(Exception::class)
    private fun createMapFiles(files: ArrayList<Foc>): ArrayList<MapFile> {
        var exception: Exception? = MapFileException("No file specified")

        val result = ArrayList<MapFile>(files.size)

        for (file in files) {
            try {
                result.add(MapFile(file.toString()))
            } catch (e: Exception) {
                exception = e
            }
        }
        if (result.size == 0) {
            throw exception!!
        }

        return result
    }

    fun destroy() {
        mapWorkerPool?.stop()
        mapDataStore?.close()
        renderThemeFuture?.decrementRefCount()
    }


    fun addJob(tile: Tile) {
        if (supportsTile(tile)) {
            jobQueue?.add(createJob(tile))
        }
    }


    private fun createJob(tile: Tile): RendererJob {
        return RendererJob(
            tile, mapDataStore,
            renderThemeFuture,
            displayModel,
            TEXT_SCALE,
            TRANSPARENT, false
        )
    }


    override fun draw(b: BoundingBox, z: Byte, c: Canvas, t: Point, rotation: Rotation) {}

    fun supportsTile(tile: Tile): Boolean {
        val mapDataStore = mapDataStore

        if (mapDataStore != null) {
            return mapDataStore.supportsTile(tile)
        }
        return false
    }

    companion object {
        private const val TRANSPARENT = false
        private const val RENDER_LABELS = true
        private const val CACHE_LABELS = false
        private const val TEXT_SCALE = 1f

        private fun createTheme(
            renderTheme: XmlRenderTheme,
            displayModel: DisplayModel
        ): RenderThemeFuture {
            val theme = RenderThemeFuture(
                instance(),
                renderTheme,
                displayModel
            )
            Thread(theme).start()
            return theme
        }
    }
}
