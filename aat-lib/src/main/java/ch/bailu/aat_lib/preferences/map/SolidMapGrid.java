package ch.bailu.aat_lib.preferences.map;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.CH1903Coordinates;
import ch.bailu.aat_lib.coordinates.OlcCoordinates;
import ch.bailu.aat_lib.coordinates.WGS84Coordinates;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.map.layer.NullLayer;
import ch.bailu.aat_lib.map.layer.grid.CH1903CenterCoordinatesLayer;
import ch.bailu.aat_lib.map.layer.grid.CH1903GridLayer;
import ch.bailu.aat_lib.map.layer.grid.PlusCodesCenterCoordinatesLayer;
import ch.bailu.aat_lib.map.layer.grid.UTMCenterCoordinatesLayer;
import ch.bailu.aat_lib.map.layer.grid.UTMGridLayer;
import ch.bailu.aat_lib.map.layer.grid.WGS84Layer;
import ch.bailu.aat_lib.preferences.SolidStaticIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public class SolidMapGrid extends SolidStaticIndexList {

    private static final String POSTFIX="_GRID";
    private static final String[] LABEL={"WGS84", "CH1903", "UTM", "Open Location Code (plus codes)", "None"};


    public SolidMapGrid(StorageInterface s, String k) {
        super(s, k + POSTFIX, LABEL);
    }


    @Override
    public String getIconResource() {
        return "view_grid";
    }

    public MapLayerInterface createGridLayer(ServicesInterface services) {
        if (this.getIndex()==0) {
            return new WGS84Layer(services, getStorage());
        }

        if (this.getIndex()==1) {
            return new CH1903GridLayer(getStorage());
        }

        if (this.getIndex()==2) {
            return new UTMGridLayer(getStorage());
        }

        if (this.getIndex()==3) {
            return new PlusCodesCenterCoordinatesLayer(services, getStorage());
        }

        return new NullLayer();
    }


    public MapLayerInterface createCenterCoordinatesLayer(ServicesInterface services) {
        if (this.getIndex()==1) {
            return new CH1903CenterCoordinatesLayer(services, getStorage());
        }

        if (this.getIndex()==2) {
            return new UTMCenterCoordinatesLayer(services, getStorage());
        }

        return new NullLayer();

    }


    public CharSequence getClipboardLabel() {
        if (this.getIndex()==3 || this.getIndex()==1) {
            return LABEL[this.getIndex()];
        }

        return LABEL[0];
    }


    public CharSequence getCode(LatLong pos) {
        if (this.getIndex()==3) {
            return new OlcCoordinates(pos).toString();
        } else if (this.getIndex() == 1) {
            return new CH1903Coordinates(pos).toString();
        }

        return WGS84Coordinates.getGeoUri(pos);
    }
}
