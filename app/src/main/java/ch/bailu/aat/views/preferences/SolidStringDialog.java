package ch.bailu.aat.views.preferences;


import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.AbsSolidType;
import ch.bailu.aat.preferences.SolidString;

public class SolidStringDialog extends AbsSolidDialog implements  DialogInterface.OnClickListener{

    private final AbsSolidType solid;
    private final String[] selection;

    private int inputIndex;



    public SolidStringDialog (SolidString s) {
        solid = s;

        final AlertDialog.Builder dialog;
        final ArrayList<String> selectionList = s.buildSelection(new ArrayList<String>(10));

        buildSelection(selectionList);

        selection = selectionList.toArray(new String[selectionList.size()]);


        dialog = createDefaultDialog(s);
        dialog.setItems(selection, this);

        dialog.create();
        dialog.show();
    }

    public void buildSelection(ArrayList<String> sel) {
        sel.add(solid.getContext().getString(R.string.enter));
        inputIndex = sel.size()-1;
    }


    @Override
    public void onClick(DialogInterface dialog, int i) {
        if (i == inputIndex) {
            new SolidStringInputDialog(solid);

        } else {
            solid.setValueFromString(selection[i]);
        }
        dialog.dismiss();
    }
}