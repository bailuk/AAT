package ch.bailu.aat_lib.util.fs;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;

public class AFile {
    public static void logErrorExists(@Nonnull Foc f) {
        AppLog.e( f.getPathName() + ": " + Res.str().file_exists());
    }

    public static void logErrorReadOnly(@Nonnull Foc f) {
        AppLog.e(f.getPathName() + ": " + Res.str().file_is_readonly());
    }

    public static void logErrorNoAccess(@Nonnull Foc f) {
        AppLog.e(f.getPathName() + ": " + Res.str().gps_noaccess());
    }
}
