package ch.bailu.aat.views.tileremover;


import android.graphics.Typeface;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidTrimIndex;
import ch.bailu.aat.services.tileremover.SourceSummaryInterface;
import ch.bailu.aat.util.ui.theme.UiTheme;

public class TileSummaryView implements View.OnClickListener {
    private final RadioButton radioButton;
    private final TextView textView;
    private final RadioGroup parent;

    public TileSummaryView(RadioGroup p, int id, UiTheme theme) {
        parent = p;

        radioButton = new RadioButton(p.getContext());
        radioButton.setId(id);
        radioButton.setOnClickListener(this);

        textView = new TextView(p.getContext());

        p.addView(radioButton);
        p.addView(textView);

        theme.content(textView);
        theme.content(radioButton);

        radioButton.setTypeface(null, Typeface.BOLD);

    }

    public void destroy() {
        parent.removeView(radioButton);
        parent.removeView(textView);
    }

    public void setTitle(String title) {
        radioButton.setText(title);
    }


    public void displaySummaryReport(StringBuilder builder, SourceSummaryInterface summary) {
        builder.setLength(0);
        textView.setText(summary.buildReport(builder).toString());
    }


    public void select() {
        radioButton.toggle();
    }

    public void select(int selected) {
        if (radioButton.getId() == selected) {
            select();
        }
    }


    @Override
    public void onClick(View v) {
        new SolidTrimIndex(new Storage(parent.getContext())).setValue(v.getId());
    }
}
