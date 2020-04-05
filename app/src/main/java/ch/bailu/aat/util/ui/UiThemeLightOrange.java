package ch.bailu.aat.util.ui;

import android.view.View;

public class UiThemeLightOrange extends UiThemeLight {



    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, AppTheme.HL_ORANGE));
    }
}
