package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ch.bailu.aat_lib.preferences.SolidIndexList;

public class SolidIndexListDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener {
    private final SolidIndexList slist;
    final AlertDialog.Builder dialog;

    public SolidIndexListDialog(Context context, SolidIndexList l) {
        slist=l;
        dialog = createDefaultDialog(context, l);
        dialog.setSingleChoiceItems(slist.getStringArray(), slist.getIndex(), this);
        dialog.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        slist.setIndex(i);
        dialog.dismiss();
    }
}
