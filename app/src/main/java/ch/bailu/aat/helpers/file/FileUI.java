package ch.bailu.aat.helpers.file;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppSelectDirectoryDialog;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;

public class FileUI {
    private final File file;
    
    public FileUI(File f) {
        file = f;
    }
    
    public void copyTo(Context context) throws IOException {
        new AppSelectDirectoryDialog(context, file);
    }

    public void copyTo(Context context, File targetDir) throws Exception {
        final File target = new File(targetDir, file.getName());

        if (target.exists()) {
            logExists(context, file);
        } else {
            new UriAccess(context, file).copy(target);
            AppLog.i(context, target.getAbsolutePath());
        }

    }
    

    public static void logExists(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + c.getString(R.string.file_exists));
    }

    public static void logReadOnly(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + " is read only.*");
    }

    public void logNoAccess(Context c, File f) {
        AppLog.e(c, f.getAbsolutePath() + " no access.*");
    }

    public boolean logPermission(Context c, File f) {
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

    /*
    public void delete(Activity a, ServiceContext sc) {
        new FileDeletionDialog(a, sc);
    }


    private class FileDeletionDialog extends AppDialog {
        private final ServiceContext scontext;
        
        public FileDeletionDialog(Activity activity, ServiceContext sc) {
            scontext = sc;
            displayYesNoDialog(activity, activity.getString(R.string.file_delete_ask), file.toString());
        }


        @Override
        protected void onPositiveClick() {
            file.delete();
            rescanDirectory(scontext);
        }
    }
*/
/*
    public void useAsOverlay(Context context) {
        new AddOverlayDialog(context, file);
    }

    public void useForMockLocation(Context context) {
        new SolidMockLocationFile(context).setValue(file.toString());
    }
*/

    
    public void rescanDirectory(ServiceContext scontext) {
        if (file.getParent().equals(
                new SolidDirectoryQuery(scontext.getContext()).getValueAsString())) {
            scontext.getDirectoryService().rescan();
        }
    }

    public CharSequence getName() {
        return file.getName();
    }
}
