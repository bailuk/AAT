package ch.bailu.aat.views.graph;

import android.content.Context;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.views.PercentageLayout;

public class GraphViewFactory  {

    public static PercentageLayout speed(Context c, String key, DispatcherInterface di, int... iid) {
        return new PercentageLayout(c).add(new DistanceSpeedGraphView(c, key, di , iid), 100);
    }

    public static PercentageLayout all(Context c, String key, DispatcherInterface di, int... iid) {
        return new PercentageLayout(c).
                add(new DistanceAltitudeGraphView(c, di, iid), 33).
                add(new DistanceSpeedGraphView(c, key, di , iid), 33).
                add(new SpmGraphView(c, di , iid), 33);
    }
}
