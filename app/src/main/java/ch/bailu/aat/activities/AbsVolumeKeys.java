package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.preferences.system.SolidVolumeKeys;

public abstract class AbsVolumeKeys extends AbsBackButton {

    private SolidVolumeKeys svolumeKeys;

    public enum EventType {UP, DOWN};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        svolumeKeys = new SolidVolumeKeys(this);
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent event) {
        return onKey(code, EventType.DOWN) || super.onKeyDown(code, event);
    }


    @Override
    public boolean onKeyUp(int code, KeyEvent event) {
        return onKey(code, EventType.UP) || super.onKeyUp(code, event);
    }


    private boolean onKey(int code, EventType type) {
        if (svolumeKeys.isEnabled()) {
            if (code == KeyEvent.KEYCODE_VOLUME_UP || code == KeyEvent.KEYCODE_VOLUME_DOWN) {
                View view = getWindow().getDecorView();

                if (onVolumePressed(view, code, type)) return true;
            }
        }
        return false;
    }


    public interface OnVolumePressed {
        boolean onVolumePressed(int code, EventType type);
    }


    private boolean onVolumePressed(View view, int code, EventType type) {
        if (view instanceof OnVolumePressed) {
            if (((OnVolumePressed) view).onVolumePressed(code, type))
                return true;
        }

        if (view instanceof ViewGroup) {
            return onVolumePressedChildren((ViewGroup) view, code, type);
        }
        return false;
    }


    private boolean onVolumePressedChildren(ViewGroup parent, int code, EventType type) {
        int count = parent.getChildCount();

        for (int i=0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (onVolumePressed(view, code, type)) return true;
        }
        return false;
    }
}
