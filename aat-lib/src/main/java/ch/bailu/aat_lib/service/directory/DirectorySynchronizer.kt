package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.gpx.GpxFileWrapper
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxStatic
import ch.bailu.aat_lib.service.directory.database.GpxDatabase
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc
import java.io.Closeable
import java.io.File

class DirectorySynchronizer(private val appContext: AppContext, private val directory: Foc) :
    Closeable {
    private var pendingHandle: ObjGpx? = null
    private var pendingPreviewGenerator: MapPreviewInterface? = null
    private var filesToAdd: FilesInDirectory? = null
    private val filesToRemove = ArrayList<String>()
    private var database: GpxDatabase? = null
    private var dbAccessTime: Long = 0
    private var canContinue = true
    private var state: State = NullState()
    private val onFileChanged = BroadcastReceiver { state.ping() }

    init {
        if (appContext.services.lock()) {
            setState(StateInit())
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private abstract class State {
        abstract fun start()
        abstract fun ping()
    }

    private fun setState(s: State) {
        if (canContinue) {
            state = s
            state.start()
        } else {
            terminate()
        }
    }

    private fun terminate(e: Exception) {
        state = StateTerminate(e)
        state.start()
    }

    private fun terminate() {
        state = StateTerminate()
        state.start()
    }

    private inner class NullState : State() {
        override fun start() {}
        override fun ping() {}
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private inner class StateInit : State() {
        /**
         * TODO: move db open into background
         */
        override fun start() {
            appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileChanged)
            try {
                database = openDatabase()
                setState(StatePrepareSync())
            } catch (e: Exception) {
                terminate(e)
            }
        }

        private fun openDatabase(): GpxDatabase {
            val dbPath = appContext.summaryConfig.getDBPath(directory)
            val query = arrayOf(GpxDbConfiguration.KEY_FILENAME)
            dbAccessTime = File(dbPath).lastModified()
            return GpxDatabase(appContext.createDataBase(), dbPath, query)
        }

        override fun ping() {}
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private inner class StatePrepareSync : State() {
        private var exception: Exception? = null
        private var backgroundTask: BackgroundTask? = object : BackgroundTask() {
            override fun bgOnProcess(appContext: AppContext): Long {
                try {
                    filesToAdd = FilesInDirectory(directory)

                    compareFileSystemWithDatabase()
                    removeFilesFromDatabase()
                } catch (e: Exception) {
                    exception = e
                } finally {
                    backgroundTask = null
                    appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, directory.path)
                }
                return 100
            }
        }

        override fun start() {
            backgroundTask?.apply {
                appContext.broadcaster.broadcast(AppBroadcaster.DBSYNC_START)
                appContext.services.backgroundService.process(this)
            }
        }

        override fun ping() {
            if (backgroundTask == null) {
                val e = exception
                if (e == null) {
                    setState(StateLoadNextGpx())
                } else {
                    terminate(e)
                }
            }
        }

        private fun removeFilesFromDatabase() {
            if (canContinue && filesToRemove.size > 0) {
                var i = 0
                while (canContinue && i < filesToRemove.size) {
                    removeFileFromDatabase(filesToRemove[i])
                    i++
                }
                appContext.broadcaster.broadcast(AppBroadcaster.DB_SYNC_CHANGED)
            }
        }

        private fun removeFileFromDatabase(name: String) {
            val file = directory.child(name)
            appContext.summaryConfig.getPreviewFile(file).rm()
            database?.deleteEntry(file)
        }

        private fun compareFileSystemWithDatabase() {
            val resultSet = database!!.query(null)
            var r = resultSet.moveToFirst()
            while (canContinue && r) {
                val name = getFileName(resultSet)
                val file = filesToAdd?.findItem(name)
                if (file == null) {
                    filesToRemove.add(name)
                } else if (isFileInSync(file)) {
                    filesToAdd?.pollItem(file)
                } else {
                    filesToRemove.add(name)
                }
                r = resultSet.moveToNext()
            }
            resultSet.close()
        }

        private fun getFileName(resultSet: DbResultSet): String {
            return resultSet.getString(GpxDbConfiguration.KEY_FILENAME)
        }

        private fun isFileInSync(file: Foc): Boolean {
            return if (file.lastModified() < System.currentTimeMillis()) {
                file.lastModified() < dbAccessTime
            } else true
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private inner class StateLoadNextGpx : State() {
        override fun start() {
            val file = filesToAdd!!.pollItem()
            if (file == null) {
                terminate()
            } else {
                val h = appContext.services.cacheService.getObject(
                    file.path, ObjGpxStatic.Factory()
                )
                if (h is ObjGpx) {
                    setPendingGpxHandle(h)
                    state.ping()
                } else {
                    h.free()
                    state.start()
                }
            }
        }

        override fun ping() {
            val handle = pendingHandle

            if (!canContinue) {
                terminate()
            } else if (handle != null && handle.isReadyAndLoaded) {
                try {
                    addGpxSummaryToDatabase(handle.id, handle.gpxList)
                    setState(StateLoadPreview())
                } catch (e: Exception) {
                    terminate(e)
                }
            } else if (handle != null && handle.hasException()) {
                state.start()
            }
        }

        private fun addGpxSummaryToDatabase(id: String, list: GpxList) {
            val file = appContext.toFoc(id)
            val keys = ArrayList<String>()
            val values = ArrayList<String>()
            createContentValues(file.name, list.getDelta(), keys, values)
            database?.insert(keys.toTypedArray(), *values.toTypedArray())
        }

        private fun createContentValues(
            filename: String,
            summary: GpxBigDeltaInterface, keys: ArrayList<String>, values: ArrayList<String>
        ) {
            val bounding = summary.getBoundingBox()
            keys.add(GpxDbConfiguration.KEY_FILENAME)
            values.add(filename)
            keys.add(GpxDbConfiguration.KEY_AVG_SPEED)
            values.add(summary.getSpeed().toString())
            keys.add(GpxDbConfiguration.KEY_MAX_SPEED)
            values.add(toNumber(summary.getAttributes()[MaxSpeed.INDEX_MAX_SPEED]))
            keys.add(GpxDbConfiguration.KEY_DISTANCE)
            values.add(summary.getDistance().toString())
            keys.add(GpxDbConfiguration.KEY_START_TIME)
            values.add(summary.getStartTime().toString())
            keys.add(GpxDbConfiguration.KEY_TOTAL_TIME)
            values.add(summary.getTimeDelta().toString())
            keys.add(GpxDbConfiguration.KEY_END_TIME)
            values.add(summary.getEndTime().toString())
            keys.add(GpxDbConfiguration.KEY_PAUSE)
            values.add(summary.getPause().toString())
            keys.add(GpxDbConfiguration.KEY_TYPE_ID)
            values.add(summary.getType().toInteger().toString())
            keys.add(GpxDbConfiguration.KEY_EAST_BOUNDING)
            values.add(bounding.lonEastE6.toString())
            keys.add(GpxDbConfiguration.KEY_WEST_BOUNDING)
            values.add(bounding.lonWestE6.toString())
            keys.add(GpxDbConfiguration.KEY_NORTH_BOUNDING)
            values.add(bounding.latNorthE6.toString())
            keys.add(GpxDbConfiguration.KEY_SOUTH_BOUNDING)
            values.add(bounding.latSouthE6.toString())
        }
    }

    private fun toNumber(s: String): String {
        return if (s == "") "0" else s
    }

    private fun setPendingGpxHandle(h: ObjGpx?) {
        pendingHandle?.free()
        pendingHandle = h
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private inner class StateLoadPreview : State() {
        override fun start() {
            val gpxFile = appContext.toFoc(pendingHandle!!.id)
            val previewImageFile = appContext.summaryConfig.getPreviewFile(gpxFile)
            val info: GpxInformation = GpxFileWrapper(gpxFile, pendingHandle!!.gpxList)
            try {
                val p = appContext.createMapPreview(info, previewImageFile)
                setPendingPreviewGenerator(p)
                state.ping()
            } catch (e: Exception) {
                AppLog.w(this, e)
                appContext.broadcaster.broadcast(AppBroadcaster.DB_SYNC_CHANGED)
                setState(StateLoadNextGpx())
            }
        }

        override fun ping() {
            val previewGenerator = pendingPreviewGenerator

            if (!canContinue || previewGenerator !is MapPreviewInterface) {
                terminate()
            } else if (previewGenerator.isReady()) {
                previewGenerator.generateBitmapFile()
                appContext.broadcaster.broadcast(AppBroadcaster.DB_SYNC_CHANGED)
                setState(StateLoadNextGpx())
            }
        }
    }

    private fun setPendingPreviewGenerator(previewGenerator: MapPreviewInterface?) {
        pendingPreviewGenerator?.onDestroy()
        pendingPreviewGenerator = previewGenerator
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private inner class StateTerminate : State {
        constructor(e: Exception) {
            AppLog.e(this, e)
        }

        constructor() {}

        override fun ping() {}
        override fun start() {
            AppLog.d(this, "state terminate")
            appContext.broadcaster.unregister(onFileChanged)
            database?.close()
            setPendingGpxHandle(null)
            setPendingPreviewGenerator(null)
            appContext.broadcaster.broadcast(AppBroadcaster.DBSYNC_DONE)
            appContext.services.free()
        }
    }

    @Synchronized
    override fun close() {
        canContinue = false
        state.ping()
    }
}
