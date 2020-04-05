package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class UiThemeLightHeader  extends UiThemeLight {

    public final static int BUTTON_GRAY = Color.GRAY;


    @Override
    public void topic(TextView v) {
        v.setTextColor(AppTheme.HL_ORANGE);
        v.setTextSize(HEADER_TEXT_SIZE *1.5f);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(AppTheme.HL_ORANGE);
        v.setTextSize(HEADER_TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getHighlightColor() {
        return AppTheme.HL_ORANGE;
    }


    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, BUTTON_GRAY));
    }
}
