package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc


abstract class IteratorAbstract(private val appContext: AppContext) : Iterator(),
    OnPreferencesChanged {
    private var onCursorChangedListener = NULL_LISTENER
    private var resultSet: DbResultSet? = null
    private val sdirectory: SolidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private var selection = ""
    private val onSyncChanged = BroadcastReceiver { _: Array<out String> -> query() }

    init {
        sdirectory.register(this)
        appContext.broadcaster.register(AppBroadcaster.DB_SYNC_CHANGED, onSyncChanged)
        openAndQuery()
    }

    override fun setOnCursorChangedListener(l: OnCursorChangedListener) {
        onCursorChangedListener = l
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sdirectory.hasKey(key)) {
            openAndQuery()
        } else if (sdirectory.containsKey(key) && selection != sdirectory.createSelectionString()) {
            query()
        }
    }

    override fun moveToPrevious(): Boolean {
        return if (resultSet != null) resultSet!!.moveToPrevious() else false
    }

    override fun moveToNext(): Boolean {
        return if (resultSet != null) resultSet!!.moveToNext() else false
    }

    override fun moveToPosition(pos: Int): Boolean {
        return if (resultSet != null) resultSet!!.moveToPosition(pos) else false
    }

    override fun getId(): Long {
        resultSet?.apply {
            return getLong(GpxDbConfiguration.KEY_ID)
        }
        return 0L
    }

    override fun getCount(): Int {
        return if (resultSet != null) resultSet!!.count else 0
    }

    override fun getPosition(): Int {
        return if (resultSet != null) resultSet!!.position else -1
    }

    abstract fun onCursorChanged(resultSet: DbResultSet?, directory: Foc?, fid: String?)
    private fun openAndQuery() {
        val fileOnOldPosition = ""
        val oldPosition = 0
        appContext.services.directoryService.openDir(sdirectory.getValueAsFile())
        updateResultFromSelection()
        moveToOldPosition(oldPosition, fileOnOldPosition)
    }

    override fun query() {
        var fileOnOldPosition = ""
        var oldPosition = 0
        if (resultSet != null) {
            oldPosition = resultSet!!.position
            fileOnOldPosition = info.file.path
            resultSet!!.close()
        }
        updateResultFromSelection()
        moveToOldPosition(oldPosition, fileOnOldPosition)
    }

    private fun updateResultFromSelection() {
        try {
            selection = sdirectory.createSelectionString()
            resultSet = appContext.services.directoryService.query(selection)
        } catch (e: Exception) {
            e(this, e.javaClass.simpleName)
        }
    }

    private fun moveToOldPosition(oldPosition: Int, fileOnOldPosition: String) {
        if (resultSet != null) {
            resultSet!!.moveToPosition(oldPosition)
            onCursorChanged(resultSet, sdirectory.getValueAsFile(), fileOnOldPosition)
            onCursorChangedListener.onCursorChanged()
        }
    }

    override fun close() {
        if (resultSet != null) resultSet!!.close()
        sdirectory.unregister(this)
        appContext.broadcaster.unregister(onSyncChanged)
        onCursorChangedListener = NULL_LISTENER
    }
}
