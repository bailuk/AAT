package ch.bailu.aat.services.sensor.list;

import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.internal.BarometerSensor;

public class SensorStateID {
    public final static int UNSCANNED = 0;
    public final static int SCANNING = 1;
    public final static int VALID = 2;
    public final static int ENABLED = 3;
    public final static int CONNECTING = 4;
    public final static int CONNECTED = 5;
    public final static int INVALID = 6;


    private int state;

    public SensorStateID(int initialState) {
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
            return (nextState == VALID || nextState == INVALID);

        } else if (state == VALID) {
            return (nextState == ENABLED);

        } else if (state == ENABLED) {
            return (nextState == VALID || nextState == CONNECTING);

        } else if (state == CONNECTING) {
            return (nextState == CONNECTED || nextState == ENABLED || nextState == VALID);

        } else if (state == CONNECTED) {
            return (nextState == ENABLED || nextState == VALID);

        } else if (state == INVALID){
            return false;

        }
        return false;

    }


    public boolean isValid() {
        return state == VALID || isEnabled();
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
        return state == CONNECTING || state == CONNECTING || state == SCANNING;
    }

    public boolean isUnscanned() {
        return state == UNSCANNED;
    }

    public boolean isScanning() {
        return state == SCANNING;
    }

    public int getState() {
        return state;
    }
}
