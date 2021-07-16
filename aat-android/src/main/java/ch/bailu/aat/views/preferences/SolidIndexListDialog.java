package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;

import ch.bailu.aat.preferences.SolidIndexList;

public class SolidIndexListDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener {
    private final SolidIndexList slist;
    final AlertDialog.Builder dialog;

    public SolidIndexListDialog(SolidIndexList l) {
        slist=l;
        dialog = createDefaultDialog(l);
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
