package ch.bailu.aat.util.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;

public abstract class AppSelectDirectoryDialog  implements  DialogInterface.OnClickListener {
    private final Foc srcFile;
    private final Foc[] directories;


    private final Context context;
    public AppSelectDirectoryDialog (Context c, Foc u) {

        context=c;
        srcFile = u;
        directories = new Foc[] {
                AppDirectory.getTrackListDirectory(context, 0),
                AppDirectory.getTrackListDirectory(context, 1),
                AppDirectory.getTrackListDirectory(context, 2),
                AppDirectory.getTrackListDirectory(context, 3),
                AppDirectory.getTrackListDirectory(context, 4),
                AppDirectory.getDataDirectory(context, AppDirectory.DIR_OVERLAY),
                AppDirectory.getDataDirectory(context, AppDirectory.DIR_IMPORT)
                };



        final String[] names = new String[directories.length];
        
        for (int i=0; i< names.length; i++) names[i]=directories[i].getName();


        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(srcFile.getName() + ": " + context.getString(R.string.file_copy));
        dialog.setItems(names, this);
        dialog.create();
        dialog.show();
    }


    @Override
    public void onClick(DialogInterface dialog, int i) {
        copyTo(context, srcFile, directories[i]);
        dialog.dismiss();
    }


    public abstract void copyTo(Context context, Foc srcFile, Foc destDirectory);

}
