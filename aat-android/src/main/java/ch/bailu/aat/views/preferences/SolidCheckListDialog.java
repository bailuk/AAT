package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;

import ch.bailu.aat.preferences.SolidCheckList;

public class SolidCheckListDialog extends AbsSolidDialog implements  OnMultiChoiceClickListener{
    private final SolidCheckList slist;


    public SolidCheckListDialog(SolidCheckList l) {
        slist=l;

        final AlertDialog.Builder dialog = createDefaultDialog(l);
        dialog.setMultiChoiceItems(slist.getStringArray(), slist.getEnabledArray(), this);
        dialog.create();
        dialog.show();
    }




    @Override
    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
        slist.setEnabled(i,isChecked);
    }
}
