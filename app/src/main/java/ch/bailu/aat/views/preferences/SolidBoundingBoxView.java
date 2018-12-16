package ch.bailu.aat.views.preferences;

import android.content.SharedPreferences;
import android.view.View;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.SolidBoundingBox;
import ch.bailu.aat.views.AbsLabelTextView;


public class SolidBoundingBoxView extends AbsLabelTextView implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final SolidBoundingBox sbounding;


    public SolidBoundingBoxView(SolidBoundingBox bounding, final MapContext map) {
        super(bounding.getContext(), bounding.getLabel());

        sbounding = bounding;
        setText(bounding.getValueAsString());


        this.setOnClickListener(v -> sbounding.setValue(new BoundingBoxE6(map.getMetrics().getBoundingBox())));
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        sbounding.register(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sbounding.hasKey(key)) {
            setText(sbounding.getValueAsString());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sbounding.unregister(this);
    }
}
