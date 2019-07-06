package ch.bailu.aat.services.sensor.list;

import ch.bailu.aat.util.ToDo;

public class SensorItemState {
    public final static int UNSCANNED = 0;
    public final static int SCANNING = 1;
    public final static int SUPPORTED = 2;
    public final static int ENABLED = 3;
    public final static int CONNECTING = 4;
    public final static int CONNECTED = 5;
    public final static int UNSUPPORTED = 6;


    private static final String[] STATE_DESCRIPTION = {
            ToDo.translate("Unscanned"),
            ToDo.translate("Scanning..."),
            ToDo.translate("Supported"),
            ToDo.translate("Not connected"),
            ToDo.translate("Connecting..."),
            ToDo.translate("Connected"),
            ToDo.translate("Not supported")
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


    public String getSensorStateDescription() {
        return STATE_DESCRIPTION[state];
    }
}
