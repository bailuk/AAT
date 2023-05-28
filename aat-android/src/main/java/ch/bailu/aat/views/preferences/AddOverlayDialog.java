package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroidFactory;

public class AddOverlayDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener{

    private final SolidCustomOverlayList slist;
    private final Foc file;


    public AddOverlayDialog (Context context, SolidCustomOverlayList l, Foc f) {
        slist=l;
        file=f;

        final AlertDialog.Builder dialog = createDefaultDialog(context,l);
        dialog.setItems(slist.getStringArray(), this);
        dialog.create();
        dialog.show();
    }


    public AddOverlayDialog(Context context, Foc f) {
        this(context, new SolidCustomOverlayList(new Storage(context), new FocAndroidFactory(context)), f);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        slist.get(i).setValueFromFile(file);
        dialog.dismiss();
    }



}
