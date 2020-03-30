package ch.bailu.aat.views.bar;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.preferences.SolidImageButton;


public class ControlBar extends LinearLayout {

    private final LinearLayout canvas;

    private final int orientation;
    private final int controlSize;

    private OnClickListener listener1, listener2;

    private final UiTheme theme;

    public ControlBar(Context context, int orientation, UiTheme theme) {
        this(context, orientation, AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT, theme);
    }


    public ControlBar(Context context, int orient, int visibleButtonCount, UiTheme theme) {
        super(context);

        final FrameLayout scroller;
        this.theme = theme;
        theme.background(this);
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


    public ImageButtonViewGroup addImageButton(int res, int size) {
        ImageButtonViewGroup button = new ImageButtonViewGroup(getContext(), res);
        button.setOnClickListener(onClickListener);
        theme.button(button);
        add(button, size);

        return button;
    }



    public View add(View v) {
        add(v, controlSize);
        return v;
    }


    public View add(View v, int size) {
        canvas.addView(v, size, controlSize);
        return v;
    }

    @Override
    public void addView(View v) {
        AppLog.w(this, "Wrong call! use \'add(View)\'");
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
        View button = new SolidImageButton(slist);
        theme.button(button);
        return add(button);
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


    public ImageButtonViewGroup addImageButton(int res) {
        return addImageButton(res, getControlSize());
    }
}
