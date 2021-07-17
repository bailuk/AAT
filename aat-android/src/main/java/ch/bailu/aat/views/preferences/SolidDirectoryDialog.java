package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidSAF;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.foc_android.FocAndroid;

public class SolidDirectoryDialog extends SolidStringDialog {

    private final Activity acontext;
    private final SolidSAF saf;

    public SolidDirectoryDialog(Activity ac, SolidFile sdirectory) {
        super(ac,sdirectory);
        acontext = ac;
        saf = new SolidSAF(sdirectory);
    }


    @Override
    protected void buildExtraSelection(Context c, ArrayList<String> sel) {
        super.buildExtraSelection(c, sel);

        if (Build.VERSION.SDK_INT >= FocAndroid.SAF_MIN_SDK)
            sel.add(c.getString(R.string.pick));
    }

    @Override
    protected void onExtraItemClick(int i) {
        if (i==1)   saf.setFromPickerActivity(acontext);
        else super.onExtraItemClick(i);
    }
}
