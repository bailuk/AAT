package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ch.bailu.aat_lib.app.AppColor;

public class UiThemeDarkGray extends UiThemeDark {
    public UiThemeDarkGray(int hl_color) {
        super(hl_color);
    }

    @Override
    public int getBackgroundColor() {
        return Color.GRAY;
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
        l.setDivider(new ColorDrawable(getBackgroundColor()));
        l.setDividerHeight(height);
        l.setSelector(android.R.color.transparent);
    }

    @Override
    public void background(View v) {
        v.setBackgroundColor(getBackgroundColor());
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(0, getHighlightColor()));
    }


    @Override
    public void topic(TextView v) {
        v.setTextColor(Color.WHITE);
        v.setTextSize(HEADER_TEXT_SIZE *1.5f);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(Color.WHITE);
        v.setTextSize(HEADER_TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(Color.WHITE);
        v.setLinkTextColor(getHighlightColor());
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(AppColor.HL_BLUE);
    }

    @Override
    public void backgroundAlt(View v) {
        v.setBackgroundColor(Color.DKGRAY);
    }

    @Override
    public void contentAlt(TextView v) {
        v.setTextColor(Color.WHITE);
    }
}
