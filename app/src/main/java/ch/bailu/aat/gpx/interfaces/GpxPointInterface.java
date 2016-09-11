package ch.bailu.aat.gpx.interfaces;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.gpx.GpxAttributes;

public interface GpxPointInterface extends IGeoPoint {
    public short getAltitude();
    public double getLongitude();
    public double getLatitude();
    public long getTimeStamp();
    public GpxAttributes getAttributes();
}
