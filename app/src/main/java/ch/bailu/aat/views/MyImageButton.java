package ch.bailu.aat.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MyImageButton extends FrameLayout  {

    private final ImageView image;

    public MyImageButton(Context context, int image_res) {
        super(context);

        image = new ImageButtonView(context, image_res);
        image.setClickable(false);
        addView(image, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

}
