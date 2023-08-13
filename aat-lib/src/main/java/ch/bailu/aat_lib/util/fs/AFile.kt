package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import javax.annotation.Nonnull

object AFile {
    @JvmStatic
    fun logErrorExists(@Nonnull f: Foc) {
        AppLog.e(this, f.pathName + ": " + Res.str().file_exists())
    }

    fun logErrorReadOnly(@Nonnull f: Foc) {
        AppLog.e(this, f.pathName + ": " + Res.str().file_is_readonly())
    }

    @JvmStatic
    fun logErrorNoAccess(@Nonnull f: Foc) {
        AppLog.e(this, f.pathName + ": " + Res.str().gps_noaccess())
    }
}
