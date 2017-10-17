package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.preferences.SolidImageButton;


public class ControlBar extends LinearLayout {

    private final LinearLayout canvas;

    private final int orientation;
    private final int controlSize;

    private OnClickListener listener1, listener2;

    public ControlBar(Context context, int orientation) {
        this(context, orientation, AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT);
    }


    public ControlBar(Context context, int orient, int visibleButtonCount) {
        super(context);

        final FrameLayout scroller;

        orientation = orient;
        controlSize = AppLayout.getBigButtonSize(context, visibleButtonCount);

        canvas = new LinearLayout(context);
        canvas.setOrientation(orientation);
        this.setOrientation(orientation);



        if (orientation == HORIZONTAL) {
            scroller = new HorizontalScrollView(context);
        } else {
            scroller = new ScrollView(context);
        }

        scroller.addView(canvas);
        super.addView(scroller);
    }


    public int getControlSize() {
        return controlSize;
    }


    public void place(int x, int y, int length) {
        int large = length;
        int small = controlSize;

        int large_spec = MeasureSpec.makeMeasureSpec(large, MeasureSpec.EXACTLY);
        int small_spec = MeasureSpec.makeMeasureSpec(small, MeasureSpec.EXACTLY);

        if (orientation == HORIZONTAL) {
            measure(large_spec, small_spec);
            layout(x, y, x+large, y+small);
        } else {
            measure(small_spec, large_spec);
            layout(x, y, x+small, y+large);
        }
    }

    public ImageButton addImageButton(int res) {
        ImageButton button = new ImageButtonView(getContext(), res);
        add(button);
        button.setOnClickListener(onClickListener);
        return button;
    }


    public View add(View v) {
        canvas.addView(v, controlSize, controlSize);
        return v;
    }


    @Override
    public void addView(View v) {
        AppLog.d(this, "Wrong call! use \'addLayer(View)\'");
        canvas.addView(v, controlSize, controlSize);
    }

    public void addSpace() {
        add(new View(getContext()));
    }


    public View addButton(View button) {
        canvas.addView(button, controlSize, controlSize);
        button.setOnClickListener(onClickListener);
        return button;

    }

    public void addIgnoreSize(View v) {
        canvas.addView(v);
    }


    public View addSolidIndexButton(SolidIndexList slist) {
        return add(new SolidImageButton(slist));
    }

    public void setOnClickListener1(OnClickListener l) {
        listener1 = l;
    }

    public void setOnClickListener2(OnClickListener l) {
        listener2 = l;
    }


    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener1 != null) listener1.onClick(v);
            if (listener2 != null) listener2.onClick(v);
        }
    };



}
