package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.helpers.Clipboard;
import ch.bailu.aat.helpers.file.FileIntent;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.preferences.AbsSolidType;

public class SolidDirectoryDialog extends AbsSolidDialog
        implements  DialogInterface.OnClickListener {
    private final AbsSolidType solid;
    private final AlertDialog.Builder dialog;
    private final String[] preselect;

    private final Activity acontext;


    public SolidDirectoryDialog (Activity context, SolidDirectory d) {
        ArrayList<String> sel = d.buildSelection(new ArrayList<String>(5));

        sel = buildFromClopboard(context, sel);

        acontext = context;
        preselect = sel.toArray(new String[sel.size()+2]);
        preselect[preselect.length-2]="Browse...*";
        preselect[preselect.length-1]="Enter...*";

        solid =d;

        dialog = createDefaultDialog(d);
        dialog.setItems(preselect, this);
        dialog.create();
        dialog.show();
    }


    private static ArrayList<String> buildFromClopboard(Context context, ArrayList<String> sel) {
        final CharSequence t = new Clipboard(context).getText();
        if (t != null && t.length()>2) {
            final String s = t.toString();
            if (s.charAt(0)=='/') sel.add(s);
        }

        return sel;
    }


    @Override
    public void onClick(DialogInterface dialog, int i) {
        if (i == preselect.length-2) {
            File file = new File(solid.getValueAsString());

            new FileIntent(file).pick(solid.getLabel(), acontext, FileIntent.PICK_SOLID_TILE_DIRECTORY);

        } else if (i == preselect.length-1) {
            new SolidStringInputDialog(solid);


        } else {
            solid.setValueFromString(preselect[i]);
        }

        dialog.dismiss();
    }
}
