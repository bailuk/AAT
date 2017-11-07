package ch.bailu.aat.services.dem.tile;

import java.util.ArrayList;

import ch.bailu.aat.util.ui.AppLog;

public class Dem3Lock {
    private final ArrayList<Object> locks = new ArrayList<>(5);


    public void lock(Object owner) {
        if (!locks.contains(owner)) {
            locks.add(owner);
        }

        //AppLog.d(this, "count " + locks.size());
    }


    public void free(Object owner) {
        locks.remove(owner);
        //AppLog.d(this, "count " + locks.size());
    }


    public boolean isLocked() {
        return locks.isEmpty() == false;
    }
}
