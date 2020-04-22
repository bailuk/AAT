package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.map.MapColor;

public class LabelOverlay extends LinearLayout {
    private final static int BACKGROUND_COLOR  = MapColor.setAlpha(Color.BLACK, 100);

    private final SparseArray<TextView> labels = new SparseArray<>(4);

    private final float defualtTextSize;

    public LabelOverlay(Context context, int gravity) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(gravity);

        defualtTextSize = new TextView(context).getTextSize();

    }

    public void setTextSizeFromHeight(int height) {
        if (labels.size() > 0) {
            float size = height / labels.size();
            size -= (size/3f);
            size = Math.min(defualtTextSize, size);

            for (int i =0; i<labels.size(); i++) {
                labels.valueAt(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            }
        }
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
