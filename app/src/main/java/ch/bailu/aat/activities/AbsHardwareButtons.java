package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.preferences.system.SolidVolumeKeys;

public abstract class AbsHardwareButtons extends AbsBackButton {

    private SolidVolumeKeys svolumeKeys;

    public enum EventType {UP, DOWN}


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
        if (code == KeyEvent.KEYCODE_VOLUME_UP || code == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (svolumeKeys.isEnabled()) {
                if (onHardwareButtonPressed(code, type)) return true;
            }
        } else if (code == KeyEvent.KEYCODE_SEARCH) { // in app search
            if (onHardwareButtonPressed(code, type)) return true;
         }

        return false;
    }


    public interface OnHardwareButtonPressed {
        boolean onHardwareButtonPressed(int code, EventType type);
    }


    private boolean onHardwareButtonPressed(int code, EventType type) {
        View view = getWindow().getDecorView();
        return onHardwareButtonPressed(view, code, type);
    }


    private boolean onHardwareButtonPressed(View view, int code, EventType type) {
        if (view instanceof OnHardwareButtonPressed) {
            if (((OnHardwareButtonPressed) view).onHardwareButtonPressed(code, type))
                return true;
        }

        if (view instanceof ViewGroup) {
            return onHardwareButtonPressedChildren((ViewGroup) view, code, type);
        }
        return false;
    }


    private boolean onHardwareButtonPressedChildren(ViewGroup parent, int code, EventType type) {
        int count = parent.getChildCount();

        for (int i=0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (onHardwareButtonPressed(view, code, type)) return true;
        }
        return false;
    }
}
