package ch.bailu.aat.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MyImageButton extends FrameLayout  {

    public MyImageButton(Context context, int image_res) {
        super(context);

        ImageView image = new ImageButtonView(context, image_res);
        image.setClickable(false);
        addView(image, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
}
