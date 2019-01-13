package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import ch.bailu.aat.R;

public class UiThemeDark implements UiTheme {

    @Override
    public void background(View v) {
        v.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void button(View v) {
        v.setBackgroundResource(R.drawable.button);

    }


    @Override
    public void topic(TextView v) {
        v.setTextColor(AppTheme.getHighlightColor());
        v.setTextSize(TEXT_SIZE);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(Color.WHITE);
        v.setTextSize(TEXT_SIZE);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(Color.LTGRAY);
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(TT_COLOR);
    }
}
