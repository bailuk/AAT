package ch.bailu.aat.views;

import android.content.Context;
import android.widget.ImageButton;

import ch.bailu.aat.helpers.AppTheme;


public class ImageButtonView extends ImageButton {
    public ImageButtonView(Context context, int res) {
        super(context);

        setImageResource(res);
        AppTheme.themify(this);
    }
}
