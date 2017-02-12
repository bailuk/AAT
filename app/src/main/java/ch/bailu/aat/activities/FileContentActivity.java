package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.preferences.VerticalScrollView;


public class FileContentActivity extends AbsFileContentActivity{


    private static final String SOLID_KEY="file_content";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(SOLID_KEY);
    }

    protected MultiView createMultiView(final String SOLID_KEY) {
        map = MapFactory.DEF(this, SOLID_KEY).content(editor_helper);


        final ContentDescription summaryData[] = {
                new NameDescription(this),
                new PathDescription(this),
                new TimeDescription(this),
                new DateDescription(this),
                new EndDateDescription(this),
                new PauseDescription(this),
                new DistanceDescription(this),
                new AverageSpeedDescription(this),
                new MaximumSpeedDescription(this),
                new CaloriesDescription(this),
                new TrackSizeDescription(this),
        };

        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this, summaryData, InfoID.FILEVIEW);

        View graph = PercentageLayout.add(this,
                        new DistanceAltitudeGraphView(this, this, InfoID.FILEVIEW),
                        new DistanceSpeedGraphView(this, this, InfoID.FILEVIEW));

        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(summary);
        mv.add(map.toView());
        mv.add(graph);
        return mv;
    }

    @Override
    protected EditorHelper createEditorHelper() {
        return new EditorHelper(getServiceContext());
    }
}
