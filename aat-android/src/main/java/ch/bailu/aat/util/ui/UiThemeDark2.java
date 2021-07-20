package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.widget.TextView;

import ch.bailu.aat_lib.app.AppColor;

public class UiThemeDark2 extends UiThemeDark {

    public UiThemeDark2(int hl_color) {
        super(hl_color);
    }

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
        v.setTextColor(AppColor.HL_BLUE);
        v.setTextSize(HEADER_TEXT_SIZE);
    }
}
