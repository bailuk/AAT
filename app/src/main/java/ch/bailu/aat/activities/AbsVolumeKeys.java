package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.preferences.SolidVolumeKeys;

public class AbsVolumeKeys extends AbsBackButton {

    private SolidVolumeKeys svolumeKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        svolumeKeys = new SolidVolumeKeys(this);
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent event) {

        if (svolumeKeys.isEnabled()) {
            if (code == KeyEvent.KEYCODE_VOLUME_UP || code == KeyEvent.KEYCODE_VOLUME_DOWN) {
                View view = getWindow().getDecorView();

                if (onVolumePressed(view, code)) return true;
            }
        }
        return super.onKeyDown(code, event);
    }


    public interface OnVolumePressed {
        boolean onVolumePressed(int code);
    }


    private boolean onVolumePressed(View view, int code) {
        if (view instanceof OnVolumePressed) {
            if (((OnVolumePressed) view).onVolumePressed(code))
                return true;
        }

        if (view instanceof ViewGroup) {
            return onVolumePressedChildren((ViewGroup) view, code);
        }
        return false;
    }


    private boolean onVolumePressedChildren(ViewGroup parent, int code) {
        int count = parent.getChildCount();

        for (int i=0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (onVolumePressed(view, code)) return true;
        }
        return false;
    }
}
