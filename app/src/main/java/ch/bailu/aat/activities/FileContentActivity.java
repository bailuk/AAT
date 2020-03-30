package ch.bailu.aat.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.description.AscendDescription;
import ch.bailu.aat.description.AveragePaceDescription;
import ch.bailu.aat.description.AveragePaceDescriptionAP;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.ContentDescriptions;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DescendDescription;
import ch.bailu.aat.description.DistanceApDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.IndexedAttributeDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseApDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeApDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.GraphViewFactory;
import ch.bailu.aat.views.preferences.VerticalScrollView;


public class FileContentActivity extends AbsFileContentActivity{

    private static final String SOLID_KEY="file_content";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ViewGroup createLayout(MainControlBar bar) {
        map = MapFactory.DEF(this, SOLID_KEY).content(editorSource);

        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this, getSummaryData(this), AppTheme.trackContent,
                InfoID.FILEVIEW, InfoID.EDITOR_OVERLAY);


        summary.add(createAttributesView());


        View graph = GraphViewFactory.all(this,this,
                InfoID.FILEVIEW, InfoID.EDITOR_OVERLAY);


        if (AppLayout.isTablet(this)) {
            return createPercentageLayout(summary, graph);
        } else {
            return createMultiView(bar, summary, graph);
        }

    }


    public static ContentDescription[] getSummaryData(Context c) {
        return new ContentDescription[] {
                new NameDescription(c),
                new PathDescription(c),
                new DateDescription(c),
                new EndDateDescription(c),

                new ContentDescriptions(
                        new TimeDescription(c),
                        new TimeApDescription(c)),

                new ContentDescriptions(
                        new PauseDescription(c),
                        new PauseApDescription(c)),

                new ContentDescriptions(
                    new DistanceDescription(c),
                    new DistanceApDescription(c)),

                new ContentDescriptions(
                        new AverageSpeedDescription(c),
                        new AverageSpeedDescriptionAP(c),
                        new MaximumSpeedDescription(c)),


                new CaloriesDescription(c),

                new ContentDescriptions(
                    new AveragePaceDescription(c),
                    new AveragePaceDescriptionAP(c)),


                new ContentDescriptions(
                    new AscendDescription(c),
                    new DescendDescription(c)),

                new ContentDescriptions(
                    new IndexedAttributeDescription.HeartRate(c),
                    new IndexedAttributeDescription.HeartBeats(c)),

                new ContentDescriptions(
                    new IndexedAttributeDescription.Cadence(c),
                    new IndexedAttributeDescription.TotalCadence(c)),

                new TrackSizeDescription(c)
        };
    }

    protected ViewGroup createMultiView(MainControlBar bar,
                                   View summary, View graph) {

        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(summary);
        mv.add(map.toView());
        mv.add(graph);

        bar.addMvNext(mv);
        return mv;
    }


    private ViewGroup createPercentageLayout(
            View summary, View graph) {

        PercentageLayout a = new PercentageLayout(this);
        a.setOrientation(AppLayout.getOrientationAlongLargeSide(this));
        a.add(map.toView(), 60);
        a.add(summary, 40);

        PercentageLayout b = new PercentageLayout(this);
        b.add(a, 70);
        b.add(graph, 30);

        return b;
    }

}
