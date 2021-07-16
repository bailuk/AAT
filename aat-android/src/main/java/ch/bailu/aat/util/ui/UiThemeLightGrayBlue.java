package ch.bailu.aat.util.ui;

import android.graphics.Typeface;
import android.widget.TextView;

public class UiThemeLightGrayBlue extends UiThemeLightGray {


    @Override
    public void header(TextView v) {
        v.setTextColor(BLUE_TEXT);
        v.setTextSize(HEADER_TEXT_SIZE);
        v.setTypeface(null, Typeface.BOLD);
    }

}
