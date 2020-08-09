package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.preferences.map.SolidOverlayFileList;
import ch.bailu.foc.Foc;


public abstract class AbsSelectOverlayDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener {

    private final SolidOverlayFileList slist;

    public AbsSelectOverlayDialog(Context context) {
        this(new SolidOverlayFileList(context));
    }

    public AbsSelectOverlayDialog (SolidOverlayFileList l) {
        slist=l;

        final AlertDialog.Builder dialog = createDefaultDialog(l);
        dialog.setItems(slist.getStringArray(), this);
        dialog.create();
        dialog.show();
    }




    @Override
    public void onClick(DialogInterface dialog, int i) {
        Foc file = slist.get(i).getValueAsFile();
        onFileSelected(slist, i, file);
        dialog.dismiss();
    }


    protected abstract void onFileSelected(SolidOverlayFileList slist, int index, Foc file);
}
