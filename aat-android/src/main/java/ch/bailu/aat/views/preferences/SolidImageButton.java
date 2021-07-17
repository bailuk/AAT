package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;


public class SolidImageButton extends ImageButtonViewGroup implements OnPreferencesChanged {

    private final SolidIndexList solid;

    public SolidImageButton(Context context, SolidIndexList s) {
        super(context, s.getIconResource());

        solid = s;
        setOnClickListener(v -> {
            if (solid.length()<3) {
                solid.cycle();
            } else {
                new SolidIndexListDialog(context, solid);
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
    public void onPreferencesChanged(StorageInterface s, String key) {
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
