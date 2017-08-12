package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.fs.foc.FocAndroid;

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
        super.buildExtraSelection(c, sel);

        if (Build.VERSION.SDK_INT >= FocAndroid.SAF_MIN_SDK)
            sel.add(c.getString(R.string.pick));
    }

    @Override
    protected void onExtraItemClick(int i) {
        if (i==1)   sdirectory.setFromPickerActivity(acontext);
        else super.onExtraItemClick(i);
    }
}
