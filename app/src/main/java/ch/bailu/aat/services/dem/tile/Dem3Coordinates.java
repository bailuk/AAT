package ch.bailu.aat.services.dem.tile;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

import ch.bailu.aat.coordinates.SrtmCoordinates;

public final class Dem3Coordinates {
    private final static double REF_LO_1=7d;
    private final static double REF_LO_2=8d;


    protected SrtmCoordinates coordinates = new SrtmCoordinates(0,0);


    public float getCellsize() {


        final float fdistance = (float) LatLongUtils.sphericalDistance(
                new LatLong(coordinates.getLatitudeE6()/1e6, REF_LO_1),
                new LatLong(coordinates.getLatitudeE6()/1e6, REF_LO_2));

        float idistance = fdistance / (Dem3Array.DIMENSION.DIM-Dem3Array.DIMENSION.OFFSET);

        if (idistance==0) idistance=50;

        return idistance;
    }

    public boolean inverseLatitude() {
        return (coordinates.getLatitudeE6()>0);
    }
    public boolean inverseLongitude() {
        return (coordinates.getLongitudeE6()<0);
    }

}
