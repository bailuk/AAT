package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.layer.NullLayer;
import ch.bailu.aat.map.layer.grid.CH1903CenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.CH1903GridLayer;
import ch.bailu.aat.map.layer.grid.UTMCenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.UTMGridLayer;
import ch.bailu.aat.map.layer.grid.WGS84Layer;

public class SolidMapGrid extends SolidStaticIndexList {

    private static final String POSTFIX="_GRID";
    
    private static final String[] LABEL={"WGS84", "CH1903", "UTM", "None"};
    
    
    public SolidMapGrid(Context context, String k) {
        super(Storage.map(context), k + POSTFIX, LABEL);
    }



    @Override
    public int getIconResource() {
        return R.drawable.view_grid;
    }

    public MapLayerInterface createGridLayer(MapContext cl) {
        if (this.getIndex()==0) {
            return new WGS84Layer(cl.getContext());
        }

        if (this.getIndex()==1) {
            return new CH1903GridLayer(cl);
        }

        if (this.getIndex()==2) {
            return new UTMGridLayer(cl);
        }

        return new NullLayer();
    }


    public MapLayerInterface createCenterCoordinatesLayer() {
        if (this.getIndex()==1) {
            return new CH1903CenterCoordinatesLayer(getContext());
        }

        if (this.getIndex()==2) {
            return new UTMCenterCoordinatesLayer(getContext());
        }

        return new NullLayer();

    }

}
