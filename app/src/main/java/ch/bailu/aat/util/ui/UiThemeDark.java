package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UiThemeDark implements UiTheme {
    public static final int COLOR_BLUEGRAY=0xff444563;


    @Override
    public int getBackgroundColor() {
        return Color.BLACK;

    }

    @Override
    public int getHighlightColor () {
        return AppTheme.HL_ORANGE;
    }

    @Override
    public int getGraphBackgroundColor() {
        return 0;
    }

    @Override
    public int getGraphLineColor() {
        return Color.DKGRAY;
    }

    @Override
    public int getGraphTextColor() {
        return Color.LTGRAY;
    }


    @Override
    public void list(ListView l) {
        int height = l.getDividerHeight();
        l.setDivider(new ColorDrawable(AppTheme.HL_ORANGE));
        l.setDividerHeight(height);
        l.setSelector(android.R.color.transparent);
    }

    @Override
    public void background(View v) {
        v.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(0, AppTheme.HL_ORANGE));
    }


    @Override
    public void topic(TextView v) {
        v.setTextColor(AppTheme.HL_ORANGE);
        v.setTextSize(HEADER_TEXT_SIZE);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(Color.WHITE);
        v.setTextSize(HEADER_TEXT_SIZE);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(Color.LTGRAY);
        v.setLinkTextColor(AppTheme.HL_ORANGE);
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(AppTheme.HL_BLUE);
    }


    @Override
    public void backgroundAlt(View v) {
        v.setBackgroundColor(COLOR_BLUEGRAY);
    }


    @Override
    public void contentAlt(TextView v) {
        v.setTextColor(Color.WHITE);
    }
}
