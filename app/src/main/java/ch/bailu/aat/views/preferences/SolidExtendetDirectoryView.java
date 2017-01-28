package ch.bailu.aat.views.preferences;

import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.preferences.SolidDirectory;

public class SolidExtendetDirectoryView extends LinearLayout{
    public SolidExtendetDirectoryView(SolidDirectory s) {
        super(s.getContext());
        setOrientation(HORIZONTAL);

        addW(new SolidStringView(s));
        addView(new SolidDirectoryMenuButton(s));
    }



    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }
}
