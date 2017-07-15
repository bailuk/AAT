package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

import ch.bailu.aat.preferences.AbsSolidType;
import ch.bailu.aat.preferences.SolidString;

public abstract class AbsSolidStringDialog extends AbsSolidDialog
        implements  DialogInterface.OnClickListener{

    private final AbsSolidType solid;
    private final String[] selection;

    private final int baseSelectionSize;


    public AbsSolidStringDialog(SolidString s)  {
        solid = s;

        final AlertDialog.Builder dialog;
        final ArrayList<String> selectionList = s.buildSelection(new ArrayList<String>(10));

        baseSelectionSize = selectionList.size();

        buildExtraSelection(s.getContext(), selectionList);

        selection = selectionList.toArray(new String[selectionList.size()]);


        dialog = createDefaultDialog(s);
        dialog.setItems(selection, this);

        dialog.create();
        dialog.show();
    }

    protected abstract void buildExtraSelection(Context c, ArrayList<String> sel);
    protected abstract void onExtraItemClick(int i);


    @Override
    public void onClick(DialogInterface dialog, int i) {
        if (i < baseSelectionSize) {
            solid.setValueFromString(selection[i]);
        } else {
            onExtraItemClick(i-baseSelectionSize);
        }
        dialog.dismiss();
    }
}
