package ch.bailu.aat.services.location;

import android.location.Location;


public final class GpsLocationInformation extends RealLocationInformation {
    private final long time = System.currentTimeMillis();

    public GpsLocationInformation(Location l, int s) {
        super(l, s);
    }


    @Override
    public long getCreationTime() {
        return time;
    }


    @Override
    public boolean isFromGPS() {
        return true;
    }




}
