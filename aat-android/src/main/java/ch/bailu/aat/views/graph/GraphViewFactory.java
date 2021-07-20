package ch.bailu.aat.views.graph;

import android.content.Context;

import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.PercentageLayout;

public class GraphViewFactory  {

    public static PercentageLayout speed(Context c, DispatcherInterface di,
                                         UiTheme theme, int... iid) {
        return new PercentageLayout(c).add(new DistanceSpeedGraphView(c, di, theme, iid), 100);
    }

    public static PercentageLayout all(Context c, DispatcherInterface di, UiTheme theme, int... iid) {
        return new PercentageLayout(c,0).
                add(new DistanceAltitudeGraphView(c, di, theme, iid).hideXLabel(), 33).
                add(new DistanceSpeedGraphView(c, di , theme, iid).hideXLabel(), 33).
                add(new SpmGraphView(c, di, theme, iid), 33);
    }
}
