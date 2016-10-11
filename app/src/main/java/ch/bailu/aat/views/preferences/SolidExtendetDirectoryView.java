package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.views.description.LabelTextView;

public class SolidExtendetDirectoryView extends LinearLayout{
    public SolidExtendetDirectoryView(SolidDirectory s) {
        super(s.getContext());
        setOrientation(HORIZONTAL);

        addW(new SolidStringView(s));
        addView(new SolidDirectoryMenuButton(s));
    }

    public SolidExtendetDirectoryView(Activity context, SolidDirectory s, ContentDescription d) {
        super(s.getContext());
        setOrientation(HORIZONTAL);

        addW(new LabelTextView(context, d));
        addView(new SolidDirectoryMenuButton(s));
    }

    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }
}
