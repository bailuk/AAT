package ch.bailu.aat.helpers;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.file.FileUI;

public class AppSelectDirectoryDialog  implements  DialogInterface.OnClickListener {
    private final File file;
    private final File directories[];
    private final String names[];
    
    private final AlertDialog.Builder dialog;

    private final Context context;
    public AppSelectDirectoryDialog (Context c, File f) throws IOException {
        context=c;
        file=f;
        directories = new File[] {
                AppDirectory.getTrackListDirectory(context, 0), 
                AppDirectory.getTrackListDirectory(context, 1),
                AppDirectory.getTrackListDirectory(context, 2),
                AppDirectory.getTrackListDirectory(context, 3),
                AppDirectory.getTrackListDirectory(context, 4),
                AppDirectory.getDataDirectory(context, AppDirectory.DIR_OVERLAY),
                AppDirectory.getDataDirectory(context, AppDirectory.DIR_IMPORT)
                };

        
        names = new String[directories.length];
        
        for (int i=0; i< names.length; i++) names[i]=directories[i].getName();


        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(file.getName() + ": " + context.getString(R.string.file_copy));
        dialog.setItems(names, this);
        dialog.create();
        dialog.show();
    }


    @Override
    public void onClick(DialogInterface dialog, int i) {
        try {
            new FileUI(file).copyTo(context, directories[i]);
        } catch (Exception e) {
            AppLog.e(context, e);
        }
        dialog.dismiss();
    }
}
