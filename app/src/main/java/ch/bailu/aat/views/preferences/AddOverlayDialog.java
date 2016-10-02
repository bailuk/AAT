package ch.bailu.aat.views.preferences;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.preferences.SolidOverlayFileList;

public class AddOverlayDialog extends AbsSolidDialog
        implements  DialogInterface.OnClickListener {

    private final SolidOverlayFileList slist;
    private final File file;


    public AddOverlayDialog (SolidOverlayFileList l, File f) {
        slist=l;
        file=f;

        final AlertDialog.Builder dialog = createDefaultDialog(l);
        dialog.setItems(slist.getStringArray(), this);
        dialog.create();
        dialog.show();
    }


    public AddOverlayDialog(Context context, File f) {
        this(new SolidOverlayFileList(context), f);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        slist.get(i).setPath(file);
        dialog.dismiss();
    }



}
