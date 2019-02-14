package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.gpx.GpxDistanceWindow;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxTimeWindow;
import ch.bailu.aat.gpx.GpxWindow;

public class SolidSpeedGraphWindow extends SolidIndexList {

    private final static String KEY="GraphWindow_";

    private final static long[] TIME_VALUES = {
            5*1000,
            10*1000,
            20*1000,
            30*1000,
            60*1000,
            5*60*1000,
            15*60*1000,
    };


    private final static float[] DISTANCE_VALUES = {
            5,
            10,
            50,
            100,
            500,
            1000,
            2000
    };

    private final DistanceDescription distanceDescription;


    public SolidSpeedGraphWindow(Context c, String key) {
        super(c, KEY + key);
        distanceDescription = new DistanceDescription(c);
    }

    public GpxWindow createWindow(GpxPointNode n) {
        int i = getIndex();

        if (i < TIME_VALUES.length)
            return new GpxTimeWindow(n, TIME_VALUES[i]);

        i-= TIME_VALUES.length;
        return new GpxDistanceWindow(n, DISTANCE_VALUES[i]);
    }


    @Override
    public int length() {
        return TIME_VALUES.length + DISTANCE_VALUES.length;
    }


    public String getValueAsString(int i) {
        if (i < TIME_VALUES.length) return TimeDescription.format(TIME_VALUES[i]);

        i -= TIME_VALUES.length;
        return distanceDescription.getDistanceDescription(DISTANCE_VALUES[i]);
    }


    @Override
    public int getIconResource() {return R.drawable.open_menu_light;}

    @Override
    public String getLabel() {return getContext().getString(R.string.average);}
}
