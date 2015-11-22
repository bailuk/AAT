package ch.bailu.aat.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class IndexListDialog implements  DialogInterface.OnClickListener {
    private SolidIndexList slist;
    AlertDialog.Builder dialog;
    
    public IndexListDialog (Context context, SolidIndexList l) {
        slist=l;
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(slist.getLabel());
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
