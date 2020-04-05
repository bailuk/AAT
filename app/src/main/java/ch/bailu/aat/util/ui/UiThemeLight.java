package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UiThemeLight implements  UiTheme {

    public static final int BG_CODE_LIGHT_GRAY = 0xFFF3F3F3;
    public static final int HL_BLUE = 0xFF136fd8;
    public static final int TXT_DARK_GRAY = 0xFF24292e;
    public static final int TXT_LINK_BLUE = 0xFF428ce0;
    public static final int BG_LIGHT_BLUE = 0xff8ca6ad;
    public static final int BUTTON_BLUE = 0x88136fd8;
    public static final int BUTTON_LIGHT_GRAY = 0x88f3f3f3;
    public static final int GRAPH_BG_DARK_GREEN = 0xff49575b;


    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public void background(View v) {
        v.setBackgroundColor(getBackgroundColor());
    }

    @Override
    public void backgroundAlt(View v) {
        v.setBackgroundColor(BG_LIGHT_BLUE);
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, BUTTON_BLUE));
    }

    @Override
    public void topic(TextView v) {
        v.setTextColor(TXT_DARK_GRAY);
        v.setTextSize(HEADER_TEXT_SIZE *1.5f);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(TXT_DARK_GRAY);
        v.setTextSize(HEADER_TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(TXT_DARK_GRAY);
        v.setLinkTextColor(TXT_LINK_BLUE);

    }

    @Override
    public void contentAlt(TextView v) {
        v.setTextColor(Color.WHITE);
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(HL_BLUE);
    }

    @Override
    public void list(ListView l) {
        l.setDividerHeight(0);
        l.setSelector(android.R.color.transparent);
    }


    @Override
    public int getHighlightColor() {
        return HL_BLUE;
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
