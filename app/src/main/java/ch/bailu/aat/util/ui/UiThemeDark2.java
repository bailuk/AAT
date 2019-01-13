package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class UiThemeDark2 extends UiThemeDark {

    @Override
    public void background(View v) {
        v.setBackgroundColor(Color.DKGRAY);
    }



    @Override
    public void header(TextView v) {
        v.setTextColor(TT_COLOR);
        v.setTextSize(TEXT_SIZE);
    }

    public void content(TextView v) {
        v.setTextColor(Color.WHITE);
    }
}
