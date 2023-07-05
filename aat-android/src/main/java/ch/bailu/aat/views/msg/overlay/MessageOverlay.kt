package ch.bailu.aat.views.msg.overlay;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import ch.bailu.aat.views.msg.AbsMsgView;

public class MessageOverlay extends LinearLayout {
    private final ArrayList<AbsMsgView> messageViews =
            new ArrayList<>(5);

    public MessageOverlay(@NonNull Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void addSpace() {
        addSpace(this);
    }

    private static void addSpace(@NonNull LinearLayout v) {
        LinearLayout space = new LinearLayout(v.getContext());
        space.setOrientation(v.getOrientation());
        space.setBackgroundColor(Color.TRANSPARENT);
        v.addView(space, new LinearLayout.LayoutParams(0, 0, 1));
    }

    public AbsMsgView add(@NonNull AbsMsgView v) {
        add(this, v);
        return v;
    }

    public AbsMsgView addR(@NonNull AbsMsgView v) {
        LinearLayout wrapper = new LinearLayout(getContext());
        wrapper.setOrientation(LinearLayout.HORIZONTAL);
        wrapper.setBackgroundColor(Color.TRANSPARENT);

        addSpace(wrapper);
        add(wrapper, v);

        addView(wrapper,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
        return v;
    }

    private void add(ViewGroup parent, AbsMsgView view) {
        parent.addView(view,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
        messageViews.add(view);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (AbsMsgView v : messageViews) {
            v.attach();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        for (AbsMsgView v : messageViews) {
            v.detach();
        }

        super.onDetachedFromWindow();
    }
}
