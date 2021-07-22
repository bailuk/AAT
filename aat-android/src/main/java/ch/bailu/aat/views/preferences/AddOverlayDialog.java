package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList;
import ch.bailu.foc.Foc;

public class AddOverlayDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener{

    private final SolidOverlayFileList slist;
    private final Foc file;


    public AddOverlayDialog (Context context, SolidOverlayFileList l, Foc f) {
        slist=l;
        file=f;

        final AlertDialog.Builder dialog = createDefaultDialog(context,l);
        dialog.setItems(slist.getStringArray(), this);
        dialog.create();
        dialog.show();
    }


    public AddOverlayDialog(Context context, Foc f) {
        this(context, new SolidOverlayFileList(new Storage(context), new AndroidFocFactory(context)), f);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        slist.get(i).setValueFromFile(file);
        dialog.dismiss();
    }



}
