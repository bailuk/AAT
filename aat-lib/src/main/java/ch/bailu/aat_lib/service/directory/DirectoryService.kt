package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.directory.database.GpxDbInterface
import ch.bailu.aat_lib.service.directory.database.GpxDatabase
import ch.bailu.aat_lib.util.fs.AFile.logErrorNoAccess
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName

class DirectoryService(private val appContext: AppContext) : VirtualService(),
    DirectoryServiceInterface {
    private var database: GpxDbInterface = GpxDbInterface.NULL_DATABASE
    private var directory: Foc = FocName("")
    private var synchronizer: DirectorySynchronizer? = null
    private var openDatabasePath = ""

    override fun openDir(dir: Foc) {
        if (dir.mkdirs() && dir.canRead()) {
            directory = dir
            open(directory)
            rescan(directory)
        } else {
            logErrorNoAccess(dir)
        }
    }

    private fun open(dir: Foc) {
        val databasePath = appContext.summaryConfig.getDatabasePath(dir)
        try {
            openDataBase(databasePath)
        } catch (e: Exception) {
            AppLog.e(this, e)
            try {
                closeDataBase()
            } catch (e: Exception) {
                AppLog.e(this, e)
            }
        }
    }

    private fun closeDataBase() {
        val oldDatabase = database
        database = GpxDbInterface.NULL_DATABASE
        openDatabasePath = ""
        oldDatabase.close()
    }

    @Throws(DbException::class)
    private fun openDataBase(databasePath: String) {
        if (databasePath != openDatabasePath) {
            val oldDatabase = database
            database = GpxDatabase(appContext.createDataBase(), databasePath)
            openDatabasePath = databasePath
            oldDatabase.close()
        }
    }

    override fun select(extraStatement: String, vararg params: Any): DbResultSet {
        return database.select(extraStatement, *params)
    }

    override fun deleteEntry(file: Foc) {
        database.deleteEntry(file)
        rescan()
    }

    override fun rescan() {
        rescan(directory)
    }

    private fun rescan(dir: Foc) {
        if (dir.canRead()) {
            stopSynchronizer()
            synchronizer = DirectorySynchronizer(appContext, dir)
        }
    }

    private fun stopSynchronizer() {
        synchronizer?.close()
        synchronizer = null
    }

    override fun close() {
        database.close()
        stopSynchronizer()
    }

    override fun appendStatusText(builder: StringBuilder) {
        builder.append("<p>Directory: ")
        builder.append(directory.pathName)
        builder.append("</p>")
    }
}
