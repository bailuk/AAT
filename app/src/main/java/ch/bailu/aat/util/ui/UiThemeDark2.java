package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.widget.TextView;

public class UiThemeDark2 extends UiThemeDark {

    @Override
    public int getBackgroundColor() {
        return Color.DKGRAY;
    }


    @Override
    public void content(TextView v) {
        v.setTextColor(Color.WHITE);
    }


    @Override
    public void header(TextView v) {
        v.setTextColor(AppTheme.COLOR_BLUE);
        v.setTextSize(HEADER_TEXT_SIZE);
    }
}
