package ch.bailu.aat.views.graph;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.preferences.SolidSpeedGraphWindow;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.preferences.SolidIndexListDialog;

public class SpeedLegendView  extends LinearLayout implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    final TextView current, average, averageAP;

    final SolidSpeedGraphWindow swindow;



    public SpeedLegendView(Context context, String key) {
        super(context);

        swindow = new SolidSpeedGraphWindow(context, key);


        setOrientation(VERTICAL);


        current = new TextView(context);
        current.setTextColor(AppTheme.getHighlightColor());
        addView(current);

        average = new TextView(context);
        average.setTextColor(AppTheme.COLOR_GREEN);
        average.setText(new AverageSpeedDescription(context).getLabel());
        addView(average);

        averageAP = new TextView(context);
        averageAP.setTextColor(AppTheme.COLOR_BLUE);
        averageAP.setText(new AverageSpeedDescriptionAP(context).getLabel());
        addView(averageAP);

        setOnClickListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        swindow.register(this);
        setText();
    }

    @Override
    public void onDetachedFromWindow() {
        swindow.unregister(this);
        super.onDetachedFromWindow();
    }


    public void setText() {
        current.setText(swindow.getLabel() + " [" + swindow.getValueAsString() + "]");
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (swindow.hasKey(key)) setText();
    }

    @Override
    public void onClick(View v) {
        new SolidIndexListDialog(swindow);
    }
}

