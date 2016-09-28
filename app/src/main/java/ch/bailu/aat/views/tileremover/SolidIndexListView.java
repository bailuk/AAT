package ch.bailu.aat.views.tileremover;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.IndexListDialog;
import ch.bailu.aat.preferences.SolidIndexList;

public class SolidIndexListView extends LinearLayout implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final TextView label, value;

    private final SolidIndexList solid;

    public SolidIndexListView(final Context context, final SolidIndexList s) {
        super(context);
        setOrientation(VERTICAL);

        solid = s;

        label = new TextView(context);
        label.setText(solid.getLabel());
        addView(label);
        AppTheme.themify(label);

        value = new TextView(context);
        value.setText(solid.getValueAsString());
        addView(value);

        AppTheme.themify(this);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOrCycle();
            }
        });
    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        value.setText(solid.getValueAsString());
        solid.register(this);
    }

    private void selectOrCycle() {
        if (solid.length()<3) {
            solid.cycle();
        } else {
            new IndexListDialog(getContext(), solid);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (solid.hasKey(key)) {
            value.setText(solid.getValueAsString());
        }
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        solid.unregister(this);
    }
}
