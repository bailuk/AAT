package ch.bailu.aat.util.fs;

import android.content.Context;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppLog;

public class FileUI {
    public static void logExists(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + c.getString(R.string.file_exists));
    }

    public static void logReadOnly(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + " is read only.*");
    }

    public static void logNoAccess(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + " no access.*");
    }

    public static boolean logPermission(Context c, File f) {
        if (f.canWrite() == false) {
            if (f.canRead() == false) {
                logNoAccess(c, f);
            } else {
                logReadOnly(c, f);
            }
            return true;
        }
        return false;
    }

}
