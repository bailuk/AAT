package ch.bailu.aat.services.sensor.list;

import android.content.Context;

import ch.bailu.aat.R;

public class SensorItemState {
    public final static int UNSCANNED = 0;
    public final static int SCANNING = 1;
    public final static int SUPPORTED = 2;
    public final static int ENABLED = 3;
    public final static int CONNECTING = 4;
    public final static int CONNECTED = 5;
    public final static int UNSUPPORTED = 6;


    // in future decouple
    // configuration, discovery: unscanned, supported, enabled, unsupported
    // from real time status : scanning, connecting, connected, disconnected trying to reconnect?

    private static final int[] STATE_DESCRIPTION = {
            R.string.sensor_state_unscanned,
            R.string.sensor_state_scanning,
            R.string.sensor_state_supported,
            R.string.sensor_state_not_connected,
            R.string.sensor_state_connecting,
            R.string.sensor_state_connected,
            R.string.sensor_state_not_supported
    };

    private int state;

    public SensorItemState(int initialState) {
        state = initialState;
    }


    public boolean setState(int nextState) {
        if (isNextStateValid(nextState)) {
            state = nextState;
            return true;
        }
        return false;
    }


    private boolean isNextStateValid(int nextState) {
        if (state == UNSCANNED) {
            return (nextState == SCANNING);

        } else if (state == SCANNING) {
            return (nextState == SUPPORTED || nextState == UNSUPPORTED);

        } else if (state == SUPPORTED) {
            return (nextState == ENABLED);

        } else if (state == ENABLED) {
            return (nextState == SUPPORTED || nextState == CONNECTING);

        } else if (state == CONNECTING) {
            return (nextState == CONNECTED || nextState == ENABLED || nextState == SUPPORTED);

        } else if (state == CONNECTED) {
            return (nextState == ENABLED || nextState == SUPPORTED);

        } else if (state == UNSUPPORTED){
            return false;

        }
        return false;

    }


    public boolean isSupported() {
        return state == SUPPORTED || isEnabled();
    }

    public boolean isEnabled() {
        return state == ENABLED || state == CONNECTING || state == CONNECTED;
    }

    public boolean isConnected() {
        return state == CONNECTED;
    }

    public boolean isConnecting() {
        return state == CONNECTING;
    }

    public boolean isOpen() {
        return state == CONNECTING || state == SCANNING;
    }

    public boolean isUnscanned() {
        return state == UNSCANNED;
    }

    public boolean isUnscanned_or_scanning() {
        return state == UNSCANNED || state == SCANNING;
    }

    public boolean isScanning() {
        return state == SCANNING;
    }

    public int getState() {
        return state;
    }


    public String getSensorStateDescription(Context c) {
        return c.getString(STATE_DESCRIPTION[state]);
    }
}
