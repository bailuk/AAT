package ch.bailu.aat.util.ui;

import android.view.View;

public class UiThemeLightOrange extends UiThemeLight {


    private final int hl_color;

    public UiThemeLightOrange(int hl_color) {
        this.hl_color = hl_color;
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, hl_color));
    }
}
