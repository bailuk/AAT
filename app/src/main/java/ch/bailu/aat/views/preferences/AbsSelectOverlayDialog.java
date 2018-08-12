package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.util_java.foc.Foc;


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
        onFileSelected(file);
        dialog.dismiss();
    }

    protected abstract void onFileSelected(Foc file);
}
