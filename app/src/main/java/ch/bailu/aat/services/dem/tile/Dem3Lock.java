package ch.bailu.aat.services.dem.tile;

import ch.bailu.aat.util.ui.AppLog;

public class Dem3Lock {

    int locks=0;

    public void lock(Object owner) {
        locks++;

    }


    public void free(Object owner) {
        locks--;
    }


    public boolean isLocked() {
        if (locks < 0) AppLog.d(this, "ERROR: negative lock: " + locks);
        return locks>0;
    }
}
