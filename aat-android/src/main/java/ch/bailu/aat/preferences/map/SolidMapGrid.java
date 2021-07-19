package ch.bailu.aat.preferences.map;

import android.content.Context;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat.map.layer.NullLayer;
import ch.bailu.aat.map.layer.grid.CH1903CenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.CH1903GridLayer;
import ch.bailu.aat.map.layer.grid.PlusCodesCenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.UTMCenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.UTMGridLayer;
import ch.bailu.aat.map.layer.grid.WGS84Layer;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.coordinates.CH1903Coordinates;
import ch.bailu.aat_lib.coordinates.OlcCoordinates;
import ch.bailu.aat_lib.coordinates.WGS84Coordinates;
import ch.bailu.aat_lib.preferences.SolidStaticIndexList;

public class SolidMapGrid extends SolidStaticIndexList {

    private static final String POSTFIX="_GRID";

    private static final String[] LABEL={"WGS84", "CH1903", "UTM", "Open Location Code (plus codes)", "None"};


    private final Context context;

    public SolidMapGrid(Context context, String k) {
        super(new Storage(context), k + POSTFIX, LABEL);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }



    @Override
    public int getIconResource() {
        return R.drawable.view_grid;
    }

    public MapLayerInterface createGridLayer(MapContext mc) {
        if (this.getIndex()==0) {
            return new WGS84Layer(new Storage(To.context(mc)));
        }

        if (this.getIndex()==1) {
            return new CH1903GridLayer(mc);
        }

        if (this.getIndex()==2) {
            return new UTMGridLayer(mc);
        }

        if (this.getIndex()==3) {
            return new PlusCodesCenterCoordinatesLayer(new Storage(getContext()));
        }

        return new NullLayer();
    }


    public MapLayerInterface createCenterCoordinatesLayer() {
        if (this.getIndex()==1) {
            return new CH1903CenterCoordinatesLayer(new Storage(getContext()));
        }

        if (this.getIndex()==2) {
            return new UTMCenterCoordinatesLayer(new Storage(getContext()));
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
