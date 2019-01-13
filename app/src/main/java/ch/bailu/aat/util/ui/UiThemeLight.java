package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import ch.bailu.aat.R;

public class UiThemeLight extends  UiThemeDark {
    public static final int COLOR_FG=Color.BLACK;
    public static final int COLOR_BG=Color.WHITE;


    @Override
    public void background(View v) {
        v.setBackgroundColor(COLOR_BG);
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
        v.setTextColor(COLOR_FG);
        v.setTextSize(TEXT_SIZE);


    }

    @Override
    public void content(TextView v) {
        v.setTextColor(Color.BLACK);
    }
}
