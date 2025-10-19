package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc

class IteratorFollowFile(appContext: AppContext) : IteratorAbstract(appContext) {
    private var info = GpxInformation.NULL

    init {
        query()
    }

    override fun getInfo(): GpxInformation {
        return info
    }

    override fun onCursorChanged(resultSet: DbResultSet, directory: Foc, fileID: String) {
        info = GpxInformationDbEntry(resultSet, directory)
        findFile(fileID)
    }

    private fun findFile(fileID: String): Boolean {
        val oldPosition = getPosition()

        moveToPosition(-1)
        while (moveToNext()) {
            if (info.getFile().path == fileID) {
                return true
            }
        }

        moveToPosition(oldPosition)
        return false
    }
}
