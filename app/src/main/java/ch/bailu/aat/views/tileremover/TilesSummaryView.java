package ch.bailu.aat.views.tileremover;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ch.bailu.aat.preferences.SolidTrimIndex;
import ch.bailu.aat.services.tileremover.TilesSummaries;
import ch.bailu.aat.services.tileremover.TilesSummaryInterface;

public class TilesSummaryView extends RadioGroup implements View.OnClickListener {

    private final RadioButton[] radioButtons = new RadioButton[TilesSummaries.SUMMARY_SIZE];
    private final TextView[] textViews = new TextView[TilesSummaries.SUMMARY_SIZE];

    private final StringBuilder builder = new StringBuilder(100);


    public TilesSummaryView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }


    public void updateInfo(final TilesSummaryInterface[] ts) {
        for (int i = 0; i < ts.length; i++) {
            if (ts[i].isValid()) {
                if (textViews[i] == null) {
                    addViews(i, ts[i].getName());
                }
                updateInfo(textViews[i], ts[i]);
            } else if (textViews[i] != null) {
                removeViews(i);
            }
        }
    }


    private void addViews(int i, String name) {
        radioButtons[i] = new RadioButton(getContext());
        radioButtons[i].setText(name);

        radioButtons[i].setOnClickListener(this);
        radioButtons[i].setId(i);

        addView(radioButtons[i]);

        if (i == new SolidTrimIndex(getContext()).getValue()) {
            radioButtons[i].setChecked(true);
        }

        textViews[i] = new TextView(getContext());
        addView(textViews[i]);
    }


    private void removeViews(int i) {
        removeView(textViews[i]);
        textViews[i] = null;

        removeView(radioButtons[i]);
        radioButtons[i] = null;
    }


    private void updateInfo(TextView sv, TilesSummaryInterface ts) {
        builder.setLength(0);
        sv.setText(ts.buildReport(builder).toString());
    }

    @Override
    public void onClick(View v) {
        new SolidTrimIndex(getContext()).setValue(v.getId());
    }
}
