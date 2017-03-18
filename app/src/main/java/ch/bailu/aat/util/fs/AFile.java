package ch.bailu.aat.util.fs;

import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import java.io.File;
import java.util.List;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppLog;

public class AFile extends JFile {
    public static void logErrorExists(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + c.getString(R.string.file_exists));
    }


    public static void logErrorReadOnly(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + " is read only.*");
    }

    public static void logErrorNoAccess(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + " no access.*");
    }


    public static void logInfoAcess(Context c, File f) {
        String msg = ": no acess.*";;
        if (canWrite(f)) {
            msg = " is writeable.*";
        } else if (canRead(f)) {
            msg = " is read only.*";
        }

        AppLog.i(c, f.getAbsolutePath() + msg);
    }


}
