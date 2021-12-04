package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.LabelTextView;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidBoundingBox;
import ch.bailu.aat_lib.preferences.StorageInterface;


public class SolidBoundingBoxView extends LabelTextView implements OnPreferencesChanged {
    private final SolidBoundingBox sbounding;


    public SolidBoundingBoxView(Context context, SolidBoundingBox bounding, final MapContext mc, UiTheme theme) {
        super(context, bounding.getLabel(), theme);

        sbounding = bounding;
        setText(bounding.getValueAsString());

        theme.button(this);
        this.setOnClickListener(v -> sbounding.setValue(new BoundingBoxE6(mc.getMetrics().getBoundingBox())));
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        sbounding.register(this);

    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
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
