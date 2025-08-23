package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.util.WithStatusText
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc

interface DirectoryServiceInterface : WithStatusText {
    fun query(selection: String): DbResultSet?

    fun openDir(dir: Foc)
    fun rescan()
    fun deleteEntry(file: Foc)
}
