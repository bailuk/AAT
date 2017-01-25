package ch.bailu.aat.views.tileremover;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.preferences.SolidTrimIndex;
import ch.bailu.aat.services.tileremover.SourceSummaries;
import ch.bailu.aat.services.tileremover.SourceSummaryInterface;

public class TilesSummaryView extends RadioGroup implements View.OnClickListener {

    private final RadioButton[] radioButtons = new RadioButton[SourceSummaries.SUMMARY_SIZE];
    private final TextView[] textViews = new TextView[SourceSummaries.SUMMARY_SIZE];

    private final StringBuilder builder = new StringBuilder(100);


    public TilesSummaryView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }


    public void updateInfo(final SourceSummaryInterface[] ts) {
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
        AppTheme.themify(radioButtons[i]);
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


    private void updateInfo(TextView sv, SourceSummaryInterface ts) {
        builder.setLength(0);
        sv.setText(ts.buildReport(builder).toString());
    }

    @Override
    public void onClick(View v) {
        new SolidTrimIndex(getContext()).setValue(v.getId());
    }
}
