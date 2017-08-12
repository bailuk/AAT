package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.util_java.foc.Foc;

public class AddOverlayDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener{

    private final SolidOverlayFileList slist;
    private final Foc file;


    public AddOverlayDialog (SolidOverlayFileList l, Foc f) {
        slist=l;
        file=f;

        final AlertDialog.Builder dialog = createDefaultDialog(l);
        dialog.setItems(slist.getStringArray(), this);
        dialog.create();
        dialog.show();
    }


    public AddOverlayDialog(Context context, Foc f) {
        this(new SolidOverlayFileList(context), f);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        slist.get(i).setValueFromFile(file);
        dialog.dismiss();
    }



}
