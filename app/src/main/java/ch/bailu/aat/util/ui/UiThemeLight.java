package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import ch.bailu.aat.R;

public class UiThemeLight implements  UiTheme {

    private final static int GITHUB_TEXT = 0xFF24292e;
    private final static int GITHUB_LINK = 0xFF428ce0;
    private final static int GITHUB_BACKGROUND=Color.WHITE;

    @Override
    public void background(View v) {
        v.setBackgroundColor(GITHUB_BACKGROUND);
    }

    @Override
    public void button(View v) {
        v.setBackgroundResource(R.drawable.button);
    }


    @Override
    public void topic(TextView v) {
        v.setTextColor(AppTheme.getHighlightColor());
        v.setTextSize(TEXT_SIZE);

    }

    @Override
    public void header(TextView v) {
        v.setTextColor(GITHUB_TEXT);
        v.setTextSize(TEXT_SIZE);


    }

    @Override
    public void content(TextView v) {
        v.setTextColor(GITHUB_TEXT);
        v.setLinkTextColor(GITHUB_LINK);

    }

    @Override
    public void toolTip(TextView v) {

    }
}
