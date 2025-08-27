package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
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
        val dbPath = appContext.summaryConfig.getDBPath(dir)

        try {
            openDataBase(dbPath)
        } catch (e: Exception) {
            database = GpxDbInterface.NULL_DATABASE
        }
    }

    @Throws(DbException::class)
    private fun openDataBase(dbPath: String) {
        database.close()
        database = GpxDatabase(appContext.createDataBase(), dbPath)
    }

    override fun select(extraSqlStatment: String, vararg params: Any): DbResultSet {
        return database.select(extraSqlStatment, *params)
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
