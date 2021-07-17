package ch.bailu.aat.views.preferences;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.SolidBoundingBox;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.LabelTextView;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;


public class SolidBoundingBoxView extends LabelTextView implements OnPreferencesChanged {
    private final SolidBoundingBox sbounding;


    public SolidBoundingBoxView(SolidBoundingBox bounding, final MapContext map, UiTheme theme) {
        super(map.getContext(), bounding.getLabel(), theme);

        sbounding = bounding;
        setText(bounding.getValueAsString());

        theme.button(this);
        this.setOnClickListener(v -> sbounding.setValue(new BoundingBoxE6(map.getMetrics().getBoundingBox())));
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
