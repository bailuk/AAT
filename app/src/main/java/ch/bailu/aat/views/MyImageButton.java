package ch.bailu.aat.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MyImageButton extends FrameLayout  {

    private final ImageButton button;

    public MyImageButton(Context context, int image_res) {
        super(context);

        //setClickable(false);


        button = new ImageButtonView(context, image_res);
        button.setClickable(false);
        addView(button, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

}
