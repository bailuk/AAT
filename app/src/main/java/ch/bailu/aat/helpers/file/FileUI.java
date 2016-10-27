package ch.bailu.aat.helpers.file;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppSelectDirectoryDialog;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;

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
