package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc

object AFile {
    @JvmStatic
    fun logErrorExists(file: Foc) {
        AppLog.e(this, file.pathName + ": " + Res.str().file_exists())
    }

    fun logErrorReadOnly(file: Foc) {
        AppLog.e(this, file.pathName + ": " + Res.str().file_is_readonly())
    }

    @JvmStatic
    fun logErrorNoAccess(file: Foc) {
        AppLog.e(this, file.pathName + ": " + Res.str().gps_noaccess())
    }
}
