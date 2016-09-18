package ch.bailu.aat.views;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.SolidIndexList;



public class ControlBar extends LinearLayout {
    public final  static int DEFAULT_VISIBLE_BUTTON_COUNT=4;


    private final FrameLayout scroller;

    private final LinearLayout canvas;

    private final int orientation;
    private final int controlSize;

    private OnClickListener listener1, listener2;

    private final ArrayList<SolidImageButton> solidButton = new ArrayList<>();

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

        if (orientation == HORIZONTAL) {
            measure(large,small);
            layout(x, y, x+large, y+small);
        } else {
            measure(small, large);
            layout(x, y, x+small, y+large);
        }
    }

    

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) scroller.layout(0,0,r-l,b-t);
    }


    public ImageButton addImageButton(int res) {
        ImageButton button = new ImageButton(getContext());
        button.setImageResource(res);
        AppTheme.themify(button);
        this.addView(button);
        button.setOnClickListener(onClickListener);
        return button;
    }


    public Button addButton(String text) {
        Button button =  new Button(getContext());
        button.setText(text);
        AppTheme.themify(button);
        this.addView(button);
        button.setOnClickListener(onClickListener);
        return button;
    }


    @Override
    public void addView(View v) {
        canvas.addView(v, controlSize, controlSize);
    }


    public void addViewIgnoreSize(View v) {
        canvas.addView(v);
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


    public ImageButton addSolidIndexButton(SolidIndexList sIndexList) {
        SolidImageButton button = new SolidImageButton(sIndexList);
        AppTheme.themify(button);
        addView(button);
        solidButton.add(button);
        return button;
    }


    public void onSharedPreferencesChanged(String key) {
        for (int i=0; i<solidButton.size(); i++) {
            solidButton.get(i).onPreferencesChanged(key);
        }
    }


}
