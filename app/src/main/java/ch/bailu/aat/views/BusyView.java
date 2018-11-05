package ch.bailu.aat.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppDensity;

public class BusyView extends LinearLayout {
    private final TextView label;

    public BusyView(Context context) {
        super(context);
        setOrientation(HORIZONTAL);

        AppDensity density = new AppDensity(context);
        int m = density.toPixel_i(2);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(m,m,m,m);

        ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        addView(progress, lp);

        label = new TextView(context);
        addView(label, lp);
    }

    public void setText(CharSequence text) {
        label.setText(text);
    }
}
