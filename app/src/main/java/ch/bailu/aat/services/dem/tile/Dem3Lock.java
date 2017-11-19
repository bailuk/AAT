package ch.bailu.aat.services.dem.tile;

import java.util.ArrayList;

import ch.bailu.aat.util.ui.AppLog;

public class Dem3Lock {
    //private final ArrayList<Object> locks = new ArrayList<>(5);

    int locks=0;

    public void lock(Object owner) {
        locks++;

        /*if (!locks.contains(owner)) {
            locks.add(owner);
        }
*/
        //AppLog.d(this, "count " + locks);
    }


    public void free(Object owner) {
        locks--;
        //locks.remove(owner);
        AppLog.d(this, "count " + locks);
    }


    public boolean isLocked() {
        if (locks < 0) AppLog.d(this, "ERROR: negative lock: " + locks);
        return locks>0;
    }
}
