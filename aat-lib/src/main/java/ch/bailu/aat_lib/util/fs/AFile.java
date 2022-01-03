package ch.bailu.aat_lib.util.fs;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.resources.ToDo;
import ch.bailu.foc.Foc;

public class AFile {
    public static void logErrorExists(Foc f) {
        AppLog.e( f.getPathName() + Res.str().file_exists());
    }

    public static void logErrorReadOnly(Foc f) {
        AppLog.e(f.getPathName() + ToDo.translate(" is read only."));
    }

    public static void logErrorNoAccess(Foc f) {
        AppLog.e(f.getPathName() + ToDo.translate(" no access."));
    }

    public static void logInfoAcess(Foc f) {
        String msg = ToDo.translate(": no acess.");
        if (f.canWrite()) {
            msg = ToDo.translate(" is writeable.");
        } else if (f.canRead()) {
            msg = ToDo.translate(" is read only.");
        }

        AppLog.i(f.getPathName() + msg);
    }
}
