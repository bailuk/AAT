package ch.bailu.aat.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;

public class ImageButtonViewGroup extends FrameLayout  {

    private final int MAX_IMAGE_SIZE = 40;
    private final ImageView imageView;

    public ImageButtonViewGroup(Context context, int image_res) {
        super(context);

        imageView = new ImageView(context);
        imageView.setImageResource(image_res);
        imageView.setClickable(false);
        addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    public void setImageResource(int image_res) {
        imageView.setImageResource(image_res);
    }


    @Override
    public void onMeasure(int wSpec, int hSpec) {
        setMinPadding(MeasureSpec.getSize(wSpec), MeasureSpec.getSize(hSpec));
        super.onMeasure(wSpec, hSpec);
    }

    private void setMinPadding(int width, int height) {
        int pix_size = Math.min(width, height);

        int size = new AppDensity(getContext()).toDensityIndependentPixel(pix_size);
        int padding = Math.max((size - MAX_IMAGE_SIZE)/2, 0);
        AppTheme.padding(this, padding);
    }
}
