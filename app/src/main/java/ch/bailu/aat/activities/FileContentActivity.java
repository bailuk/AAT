package ch.bailu.aat.activities;

import android.os.Bundle;

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
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.TrackDescriptionView;
import ch.bailu.aat.views.description.VerticalView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.map.MapFactory;
import ch.bailu.aat.views.preferences.VerticalScrollView;


public class FileContentActivity extends AbsFileContentActivity{


    private static final String SOLID_KEY="file_content";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(SOLID_KEY);
    }

    protected MultiView createMultiView(final String SOLID_KEY) {
        map = new MapFactory(this, SOLID_KEY).content(editor_helper);


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
        VerticalView graph = new VerticalView(this, SOLID_KEY,
                InfoID.FILEVIEW,
                new TrackDescriptionView[] {
                        new DistanceAltitudeGraphView(this, SOLID_KEY),
                        new DistanceSpeedGraphView(this, SOLID_KEY)});


        MultiView mv = new MultiView(this, SOLID_KEY, InfoID.ALL);
        mv.add(summary, summary.addAllContent(summaryData, InfoID.FILEVIEW));
        mv.add(map);
        mv.addT(graph);
        return mv;
    }

    @Override
    protected EditorHelper createEditorHelper() {
        return new EditorHelper(getServiceContext());
    }
}
