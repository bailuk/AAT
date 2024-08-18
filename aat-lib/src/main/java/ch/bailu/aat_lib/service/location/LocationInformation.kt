package ch.bailu.aat_lib.service.location;


import ch.bailu.aat_lib.gpx.GpxInformation;

public abstract class LocationInformation extends GpxInformation {
    public abstract boolean hasAccuracy();
    public abstract boolean hasSpeed();
    public abstract boolean hasAltitude();
    public abstract boolean hasBearing();


    public abstract boolean isFromGPS();
    public abstract long getCreationTime();

    public abstract void setAltitude(double altitude);
}
