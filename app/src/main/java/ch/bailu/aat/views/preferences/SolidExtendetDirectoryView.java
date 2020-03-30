package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.ui.UiTheme;

public class SolidExtendetDirectoryView extends LinearLayout{
    public SolidExtendetDirectoryView(Activity a, SolidFile s, UiTheme theme) {
        super(s.getContext());
        setOrientation(HORIZONTAL);

        addW(new SolidStringView(s, theme));
        addView(new SolidDirectoryMenuButton(a,s));
    }



    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }
}
