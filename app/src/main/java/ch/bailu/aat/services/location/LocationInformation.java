package ch.bailu.aat.services.location;

import ch.bailu.aat.gpx.GpxInformation;

public abstract class LocationInformation extends GpxInformation {
    public abstract boolean hasAccuracy();
    public abstract boolean hasSpeed();
    public abstract boolean hasAltitude();
    public abstract boolean hasBearing();
}
