package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc


abstract class IteratorAbstract(private val appContext: AppContext) : Iterator(),
    OnPreferencesChanged {
    private var onCursorChangedListener = { }
    private var resultSet: DbResultSet? = null
    private val sdirectory: SolidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private var selection = ""
    private val onSyncChanged = BroadcastReceiver { _: Array<out String> -> query() }

    init {
        sdirectory.register(this)
        appContext.broadcaster.register(AppBroadcaster.DB_SYNC_CHANGED, onSyncChanged)
        openAndQuery()
    }

    override fun setOnCursorChangedListener(listner: () -> Unit) {
        onCursorChangedListener = listner
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sdirectory.hasKey(key)) {
            openAndQuery()
        } else if (sdirectory.containsKey(key) && selection != sdirectory.createSelectionString()) {
            query()
        }
    }

    override fun moveToPrevious(): Boolean {
        return resultSet?.moveToPrevious() ?: false
    }

    override fun moveToNext(): Boolean {
        return resultSet?.moveToNext() ?: false
    }

    override fun moveToPosition(pos: Int): Boolean {
        return resultSet?.moveToPosition(pos) ?: false
    }

    override fun getID(): Long {
        resultSet?.apply {
            return getLong(GpxDbConfiguration.KEY_ID)
        }
        return 0L
    }

    override fun getCount(): Int {
        return resultSet?.count ?: 0
    }

    override fun getPosition(): Int {
        return resultSet?.position ?: -1
    }

    abstract fun onCursorChanged(resultSet: DbResultSet, directory: Foc, fileID: String)

    private fun openAndQuery() {
        val fileOnOldPosition = ""
        val oldPosition = 0
        appContext.services.getDirectoryService().openDir(sdirectory.getValueAsFile())
        updateResultFromSelection()
        moveToOldPosition(oldPosition, fileOnOldPosition)
    }

    override fun query() {
        var fileOnOldPosition = ""
        var oldPosition = 0
        val resultSet = resultSet
        if (resultSet is DbResultSet) {
            oldPosition = resultSet.position
            fileOnOldPosition = getInfo().getFile().path
            resultSet.close()
        }
        updateResultFromSelection()
        moveToOldPosition(oldPosition, fileOnOldPosition)
    }

    private fun updateResultFromSelection() {
        try {
            selection = sdirectory.createSelectionString()
            resultSet = appContext.services.getDirectoryService().query(selection)
        } catch (e: Exception) {
            AppLog.e(this, e.javaClass.simpleName)
        }
    }

    private fun moveToOldPosition(oldPosition: Int, fileOnOldPosition: String) {
        val resultSet = resultSet
        if (resultSet is DbResultSet) {
            resultSet.moveToPosition(oldPosition)
            onCursorChanged(resultSet, sdirectory.getValueAsFile(), fileOnOldPosition)
            onCursorChangedListener()
        }
    }

    override fun close() {
        resultSet?.close()
        sdirectory.unregister(this)
        appContext.broadcaster.unregister(onSyncChanged)
        onCursorChangedListener = { }
    }
}
