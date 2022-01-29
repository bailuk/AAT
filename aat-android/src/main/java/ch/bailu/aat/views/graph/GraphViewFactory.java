package ch.bailu.aat.views.graph;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.view.graph.DistanceAltitudePlotter;
import ch.bailu.aat_lib.view.graph.DistanceSpeedPlotter;
import ch.bailu.aat_lib.view.graph.Plotter;
import ch.bailu.aat_lib.view.graph.SpmPlotter;

public class GraphViewFactory  {

    public static PercentageLayout speed(Context c, DispatcherInterface di,
                                         UiTheme theme, int... iid) {
        return new PercentageLayout(c).add(createSpeedGraph(c, theme).connect(di, iid), 100);
    }

    public static PercentageLayout all(Context c, DispatcherInterface di, UiTheme theme, int... iid) {
        return new PercentageLayout(c,0).
                add(createAltitudeGraph(c, theme).connect(di, iid).hideXLabel(), 33).
                add(createSpeedGraph(c, theme).connect(di, iid).hideXLabel(), 33).
                add(createSpmGraph(c,theme).connect(di, iid), 33);
    }

    public static GraphView createSpeedGraph(Context context, UiTheme theme) {
        Plotter plotter = new DistanceSpeedPlotter(new SolidUnit(new Storage(context)));
        return new GraphView(context, plotter, theme);
    }


    public static GraphView createSpmGraph(Context context, UiTheme theme) {
        Plotter plotter = new SpmPlotter(new SolidUnit(new Storage(context)));
        return new GraphView(context, plotter, theme);
    }

    public static GraphView createAltitudeGraph(Context context, UiTheme theme) {
        Plotter plotter = new DistanceAltitudePlotter(new SolidUnit(new Storage(context)));
        return new GraphView(context, plotter, theme);
    }
}
