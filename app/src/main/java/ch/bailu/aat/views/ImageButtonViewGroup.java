package ch.bailu.aat.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;

public class ImageButtonViewGroup extends FrameLayout  {

    private final ImageView imageView;

    public ImageButtonViewGroup(Context context, int image_res) {
        super(context);

        imageView = new ImageView(context);
        imageView.setImageResource(image_res);
        imageView.setClickable(false);
        AppTheme.paddingV(imageView, 15);


        addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    public void setImageResource(int image_res) {
        imageView.setImageResource(image_res);
    }

}
