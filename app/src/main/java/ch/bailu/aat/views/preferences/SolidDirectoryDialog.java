package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidFile;

public class SolidDirectoryDialog extends SolidStringDialog {

    private final Activity acontext;
    private final SolidFile sdirectory;

    public SolidDirectoryDialog(Activity ac, SolidFile s) {
        super(s);
        acontext = ac;
        sdirectory=s;
    }


    @Override
    protected void buildExtraSelection(Context c, ArrayList<String> sel) {
        sel.add("Pick...*");
        super.buildExtraSelection(c, sel);
    }

    @Override
    protected void onExtraItemClick(int i) {
        if (i==0)  sdirectory.setFromPickerActivity(acontext);
        else super.onExtraItemClick(i);
    }
}
