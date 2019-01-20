package ch.bailu.aat.views;

import android.content.Context;
import android.widget.ImageButton;

import ch.bailu.aat.util.ui.AppTheme;


public class ImageButtonView extends ImageButton {
    public ImageButtonView(Context context, int res) {
        super(context);

        setImageResource(res);
        AppTheme.bar.button(this);
    }
}
