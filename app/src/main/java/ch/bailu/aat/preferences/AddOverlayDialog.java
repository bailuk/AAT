package ch.bailu.aat.preferences;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AddOverlayDialog implements  DialogInterface.OnClickListener {
    private final SolidOverlayFileList slist;
    private final File file;

    private final AlertDialog.Builder dialog;

    public AddOverlayDialog (Context context, SolidOverlayFileList l, File f) {
        slist=l;
        file=f;


        dialog = new AlertDialog.Builder(context);
        dialog.setItems(slist.getStringArray(), this);
        dialog.create();
        dialog.show();
    }


    public AddOverlayDialog(Context context, File f) {
        this(context, new SolidOverlayFileList(context), f);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        slist.get(i).setPath(file);
        dialog.dismiss();
    }
}
