package ch.bailu.aat_lib.coordinates;

import com.google.openlocationcode.OpenLocationCode;

import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

public class OlcCoordinates extends Coordinates {
    /**
     * The Open Location Code (OLC) is a geocode system for identifying
     * an area anywhere on the Earth.[1] It was developed at Google's Zürich
     * engineering office,[2] and released late October 2014.[3] Location
     * codes created by the OLC system are referred to as "plus codes".
     * https://en.wikipedia.org/wiki/Open_Location_Code
     *
     *
     * This is a wrapper around
     * com.google.openlocationcode.OpenLocationCode
     *
     */


    private final OpenLocationCode olc;


    public OlcCoordinates(String code, LatLong reference) {
        this (code, reference.getLatitude(), reference.getLongitude());
    }


    public OlcCoordinates(String code, double la, double lo) {
        OpenLocationCode olc = new OpenLocationCode(code);

        if (olc.isShort()) {
            this.olc = olc.recover(la, lo);
        } else {
            this.olc = olc;
        }
    }

    public OlcCoordinates(String code) {
        olc = new OpenLocationCode(code);
    }


    public OlcCoordinates(double la, double lo) {
        olc = new OpenLocationCode(la, lo);
    }

    public OlcCoordinates(LatLong c) {
        olc = new OpenLocationCode(c.getLatitude(), c.getLongitude());
    }


    @Nonnull
    @Override
    public String toString() {
        return olc.getCode();
    }

    @Override
    public LatLong toLatLong() {
        OpenLocationCode.CodeArea area = olc.decode();
        return new LatLong(area.getCenterLatitude(), area.getCenterLongitude());
    }
}
