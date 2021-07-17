package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.preferences.SolidFile;

public class SolidExtendetDirectoryView extends LinearLayout{
    public SolidExtendetDirectoryView(Activity a, SolidFile s, UiTheme theme) {
        super(a);
        setOrientation(HORIZONTAL);

        addW(new SolidStringView(a,s, theme));
        addView(new SolidDirectoryMenuButton(a,s));
    }



    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }
}
