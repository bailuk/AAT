package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class UiThemeLightHeader  extends UiThemeLight {

    private final static int HIGHLIGHT = AppTheme.COLOR_ORANGE;
    private final static int BUTTON_PRESSED = Color.GRAY;
    private final static int BUTTON = 0x88f3f3f3;


    @Override
    public void topic(TextView v) {
        v.setTextColor(HIGHLIGHT);
        v.setTextSize(TEXT_SIZE*1.5f);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(HIGHLIGHT);
        v.setTextSize(TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getHighlightColor() {
        return HIGHLIGHT;
    }


    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON, BUTTON_PRESSED));
    }

}
