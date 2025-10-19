package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc

class IteratorSimple(appContext: AppContext) : IteratorAbstract(appContext) {
    private var info = GpxInformation.NULL

    init {
        query()
    }

    override fun getInfo(): GpxInformation {
        return info
    }

    override fun onCursorChanged(resultSet: DbResultSet, directory: Foc, fileID: String) {
        info = if (resultSet.getCount() > 0) {
            GpxInformationDbEntry(resultSet, directory)
        } else {
            GpxInformation.NULL
        }
    }
}
