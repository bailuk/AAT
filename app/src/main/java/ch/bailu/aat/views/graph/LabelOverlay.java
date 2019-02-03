package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.map.MapColor;

public class LabelOverlay extends LinearLayout {
    private final static int BACKGROUND_COLOR  = MapColor.setAlpha(Color.BLACK, 100);

    private final SparseArray<TextView> labels = new SparseArray<TextView>(4);

    public LabelOverlay(Context context, int gravity) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(gravity);
    }


    public void setText(int color, String label, String unit) {
        setText(color, label + " [" + unit + "]");
    }


    public void setText(int color, int label, String unit) {
        setText(color, getContext().getString(label), unit);
    }


    public void setText(int color, String text) {
        final TextView v = labels.get(color);

        if (v == null) {
            labels.put(color, addLabel(color, text));
        } else {
            v.setText(text);
        }
    }


    private TextView addLabel(int color, String text) {
        final TextView v = new TextView(getContext());

        v.setText(text);
        v.setTextColor(color);
        v.setBackgroundColor(BACKGROUND_COLOR);

        addView(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        return v;
    }
}
