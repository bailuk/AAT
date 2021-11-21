package ch.bailu.aat.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.To;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.preferences.VerticalScrollView;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.NameDescription;
import ch.bailu.aat_lib.description.PathDescription;
import ch.bailu.aat_lib.description.TrackSizeDescription;
import ch.bailu.aat_lib.gpx.InfoID;

public class GpxEditorActivity extends AbsFileContentActivity {

    private static final String SOLID_KEY="gpx_editor";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected ViewGroup createLayout(MainControlBar bar, ContentView contentView) {
        map = MapFactory.DEF(getAppContext(),this, SOLID_KEY).editor(editorSource);


        ContentDescription[] summaryData = {
                new NameDescription(),
                new PathDescription(),
                new DistanceDescription(getStorage()),
                new TrackSizeDescription(),
        };


        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this, summaryData,
                AppTheme.trackContent,
                InfoID.EDITOR_OVERLAY,
                InfoID.FILEVIEW);

        summary.add(createAttributesView());

        DistanceAltitudeGraphView graph = new DistanceAltitudeGraphView(this,
                this, THEME,
                InfoID.EDITOR_OVERLAY,
                InfoID.FILEVIEW);


        if (AppLayout.isTablet(this)) {
            return createPercentageLayout(summary, graph);
        } else {
            return createMultiView(bar, summary, graph, contentView);
        }

    }


    protected ViewGroup createMultiView(MainControlBar bar,
                                   View summary, View graph, ContentView contentView) {

        MultiView mv = new MultiView(this, SOLID_KEY);

        mv.add(To.view(map));

        PercentageLayout p = new PercentageLayout(this);
        p.add(summary,60);
        p.add(graph,40);
        mv.add(p);
        bar.addMvNext(mv);
        contentView.addMvIndicator(mv);
        return mv;
    }


    private ViewGroup createPercentageLayout(
            View summary, View graph) {

        if (AppLayout.getOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            PercentageLayout a = new PercentageLayout(this);
            a.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

            a.add(To.view(map), 60);
            a.add(summary, 40);

            PercentageLayout b = new PercentageLayout(this);
            b.add(a, 85);
            b.add(graph, 15);

            return b;
        } else {
            PercentageLayout a = new PercentageLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            a.add(To.view(map),100);

            PercentageLayout b = new PercentageLayout(this);
            b.add(a, 70);
            b.add(summary, 30);


            PercentageLayout c = new PercentageLayout(this);
            c.add(b, 85);
            c.add(graph,15);

            return c;
        }
    }
}
