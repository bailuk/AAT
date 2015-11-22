package ch.bailu.aat.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;

public class CheckListDialog implements  OnMultiChoiceClickListener{
    private final SolidCheckList slist;

    private final AlertDialog.Builder dialog;

    public CheckListDialog (Context context, SolidCheckList l) {
        slist=l;

        dialog = new AlertDialog.Builder(context);
        dialog.setMultiChoiceItems(slist.getStringArray(), slist.getEnabledArray(), this);
        dialog.create();
        dialog.show();
    }




    @Override
    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
        slist.setEnabled(i,isChecked);
    }
}
