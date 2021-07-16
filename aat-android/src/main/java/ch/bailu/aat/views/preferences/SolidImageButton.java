package ch.bailu.aat.views.preferences;

import android.content.SharedPreferences;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.views.ImageButtonViewGroup;


public class SolidImageButton extends ImageButtonViewGroup implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final SolidIndexList solid;

    public SolidImageButton(SolidIndexList s) {
        super(s.getContext(), s.getIconResource());

        solid = s;
        setOnClickListener(v -> {
            if (solid.length()<3) {
                solid.cycle();
            } else {
                new SolidIndexListDialog(solid);
            }

        });
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setImageResource(solid.getIconResource());
        solid.register(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (solid.hasKey(key)) {
            setImageResource(solid.getIconResource());
            AppLog.i(getContext(), solid.getValueAsString());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        solid.unregister(this);
    }
}
