package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UiThemeLightGray implements  UiTheme {

    public static final int DARK_TEXT = 0xFF24292e;
    public static final int LIGHT_GRAY = 0xFFf3f3f3;
    public static final int DARKER_GRAY = 0xFFdedede;
    public static final int GRAPH_BG_DARK_GREEN = 0xff49575b;
    public static final int BLUE_TEXT = 0xFF3C3B6E;


    @Override
    public int getBackgroundColor() {
        return DARKER_GRAY;
    }

    @Override
    public void background(View v) {
        v.setBackgroundColor(getBackgroundColor());
    }

    @Override
    public void backgroundAlt(View v) {
        v.setBackgroundColor(Color.DKGRAY);
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(LIGHT_GRAY, AppTheme.HL_GREEN));
    }

    @Override
    public void topic(TextView v) {
        v.setTextColor(DARK_TEXT);
        v.setTextSize(HEADER_TEXT_SIZE *1.5f);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(DARK_TEXT);
        v.setTextSize(HEADER_TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(DARK_TEXT);
        v.setLinkTextColor(BLUE_TEXT);

    }

    @Override
    public void contentAlt(TextView v) {
        v.setTextColor(Color.WHITE);
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(BLUE_TEXT);
    }

    @Override
    public void list(ListView l) {
        l.setDividerHeight(0);
        l.setSelector(android.R.color.transparent);
    }


    @Override
    public int getHighlightColor() {
        return AppTheme.HL_GREEN;
    }

    @Override
    public int getGraphBackgroundColor() {
        return GRAPH_BG_DARK_GREEN;
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
