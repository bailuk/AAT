package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UiThemeDarkGreen implements UiTheme {

    private final static int HIGHLIGHT = AppTheme.COLOR_ORANGE;
    private final static int TEXT = Color.WHITE;
    private final static int LINK = 0xFF428ce0;
    private final static int BACKGROUND = 0xff49575b;
    private final static int ALT_BACKGOUND=0xff8ca6ad;
    private final static int BUTTON_PRESSED = HIGHLIGHT;
    private final static int BUTTON = BACKGROUND;


    @Override
    public int getBackgroundColor() {
        return BACKGROUND;
    }

    @Override
    public void background(View v) {
        v.setBackgroundColor(BACKGROUND);
    }

    @Override
    public void backgroundAlt(View v) {
        v.setBackgroundColor(ALT_BACKGOUND);
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON, BUTTON_PRESSED));
    }

    @Override
    public void topic(TextView v) {
        v.setTextColor(TEXT);
        v.setTextSize(TEXT_SIZE*1.5f);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(TEXT);
        v.setTextSize(TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(TEXT);
        v.setLinkTextColor(LINK);

    }

    @Override
    public void contentAlt(TextView v) {
        v.setTextColor(Color.WHITE);
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(HIGHLIGHT);
    }

    @Override
    public void list(ListView l) {
//      int height = l.getDividerHeight();
//      l.setDivider(new ColorDrawable(HIGHLIGHT));
//      l.setDividerHeight(height);
        l.setDividerHeight(0);
        l.setSelector(android.R.color.transparent);
    }


    @Override
    public int getHighlightColor() {
        return HIGHLIGHT;
    }

    @Override
    public int getGraphBackgroundColor() {
        return BACKGROUND;
    }

    @Override
    public int getGraphLineColor() {
        return Color.BLACK;
    }

    @Override
    public int getGraphTextColor() {
        return Color.WHITE;
    }
}
