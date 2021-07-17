package ch.bailu.aat.util.fs;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.MemSize;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.foc.Foc;

public class AFile extends MemSize {
    public static void logErrorExists(Context c, Foc f) {
        AppLog.e(c, f.getPathName() + c.getString(R.string.file_exists));
    }


    public static void logErrorReadOnly(Context c, Foc f) {
        AppLog.e(c, f.getPathName() + " is read only.*");
    }

    public static void logErrorNoAccess(Context c, Foc f) {
        AppLog.e(c, f.getPathName() + " no access.*");
    }


    public static void logInfoAcess(Context c, Foc f) {
        String msg = ": no acess.*";
        if (f.canWrite()) {
            msg = " is writeable.*";
        } else if (f.canRead()) {
            msg = " is read only.*";
        }

        AppLog.i(c, f.getPathName() + msg);
    }


}
