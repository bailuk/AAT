package ch.bailu.aat.util.fs;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

public class AFile extends JFile {
    public static void logErrorExists(Context c, Foc f) {
        AppLog.e(c, f.toString() + c.getString(R.string.file_exists));
    }


    public static void logErrorReadOnly(Context c, Foc f) {
        AppLog.e(c, f.toString() + " is read only.*");
    }

    public static void logErrorNoAccess(Context c, Foc f) {
        AppLog.e(c, f.toString() + " no access.*");
    }


    public static void logInfoAcess(Context c, Foc f) {
        String msg = ": no acess.*";
        if (f.canWrite()) {
            msg = " is writeable.*";
        } else if (f.canRead()) {
            msg = " is read only.*";
        }

        AppLog.i(c, f.toString() + msg);
    }


}
