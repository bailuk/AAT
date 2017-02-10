package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Build;
import android.view.View;

import ch.bailu.aat.map.mapsforge.MapsForgeView;

public class SolidMapViewAcceleration extends SolidIndexList {

     private static final String[] labels = {
            "Hardware*",
            "Software*",
            "None*"
    };

    public void setLayerType(MapsForgeView v) {
        if (Build.VERSION.SDK_INT >= 11) {
            final int[] values = {
                    View.LAYER_TYPE_HARDWARE,
                    View.LAYER_TYPE_SOFTWARE,
                    View.LAYER_TYPE_NONE
            };
            v.setLayerType(values[getIndex()], null);
        }

    }
    public SolidMapViewAcceleration(Context c) {
        super(  Storage.global(c),
                SolidMapViewAcceleration.class.getSimpleName());
    }

    @Override
    public int length() {
        return labels.length;
    }

    @Override
    protected String getValueAsString(int i) {
        return labels[i];
    }


    @Override
    public String getLabel() {
        return "Acceleration*";
    }


}
