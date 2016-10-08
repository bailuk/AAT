package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.SolidIndexList;



public class ControlBar extends LinearLayout {
    public final  static int DEFAULT_VISIBLE_BUTTON_COUNT=4;

    private final FrameLayout scroller;
    private final LinearLayout canvas;

    private final int orientation;
    private final int controlSize;

    private OnClickListener listener1, listener2;

    public ControlBar(Context context, int orientation) {
        this(context, orientation, DEFAULT_VISIBLE_BUTTON_COUNT);
    }


    public ControlBar(Context context, int orient, int visibleButtonCount) {
        super(context);

        orientation = orient;
        controlSize = AppTheme.getBigButtonSize(context, visibleButtonCount);

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


/*
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) scroller.layout(0,0,r-l,b-t);
    }
*/

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
        canvas.addView(v, controlSize, controlSize);
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
