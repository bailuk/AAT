package ch.bailu.aat.activities;

import java.io.File;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.bailu.aat.R;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.FileAction;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.SummaryListView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.VerticalView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;

public class GpxViewActivity extends AbsDispatcher implements OnClickListener {

    private static final String SOLID_KEY=GpxViewActivity.class.getSimpleName();


    private LinearLayout contentView;

    private ImageButton        nextView, fileOperation, copyTo;
    private BusyButton         busyButton;
    private MultiView          multiView;
    private OsmInteractiveView map;

    private String fileID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileID = getIntent().getData().getEncodedPath();
        

        contentView = new ContentView(this);
        contentView.addView(createButtonBar());
        multiView = createMultiView();
        contentView.addView(multiView);
        setContentView(contentView);

        createDispatcher();
    }



    private ControlBar createButtonBar() {
        MainControlBar bar = new MainControlBar(this);

        nextView = bar.addImageButton(R.drawable.go_next_inverse);
        copyTo = bar.addImageButton(R.drawable.document_save_as_inverse);
        
        fileOperation = bar.addImageButton(R.drawable.edit_select_all_inverse);

        busyButton = bar.getMenu();
        busyButton.startWaiting();

        bar.setOrientation(AppLayout.getOrientationAlongSmallSide(this));
        bar.setOnClickListener1(this);
        return bar;
    }


    private MultiView createMultiView() {
        map = new OsmInteractiveView(getServiceContext(), SOLID_KEY);

        final OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_FILEVIEW),
                new CurrentLocationOverlay(map),
                new GridDynOverlay(map, getServiceContext()),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),

        };
        map.setOverlayList(overlayList);


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

        final TrackDescriptionView viewData[] = {
                new SummaryListView(this, SOLID_KEY, INFO_ID_FILEVIEW, summaryData), 
                map,
                new VerticalView(this, SOLID_KEY, INFO_ID_FILEVIEW, new TrackDescriptionView[] {
                        new DistanceAltitudeGraphView(this, SOLID_KEY),
                        new DistanceSpeedGraphView(this, SOLID_KEY)
                })
        };   

        return new MultiView(this, SOLID_KEY, INFO_ID_ALL, viewData);
    }


    private void createDispatcher() {
        final ContentSource[] source = new ContentSource[] {
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext()),
                new OverlaySource(getServiceContext()),
                new CustomFileSource(getServiceContext(), fileID)
        };

        final DescriptionInterface[] target = new DescriptionInterface[] {
                multiView, this, busyButton.getBusyControl(GpxInformation.ID.INFO_ID_FILEVIEW) 
        };
        setDispatcher(new ContentDispatcher(this,source, target));

    }




    @Override
    public void updateGpxContent(GpxInformation info) {
        if (info.getID()== INFO_ID_FILEVIEW) {
            map.frameBoundingBox(info.getBoundingBox());
        }
    }
    

    @Override
    public void onClick(View v) {
        if (v == nextView) {
            multiView.setNext();

        } else if (v == copyTo) {
            new FileAction(this, new File(fileID)).copyTo();
            
        } else if (v == fileOperation) {
            new FileAction(this, new File(fileID)).showPopupMenu(v);
        } 

    }



    @Override
    public void onServicesUp(boolean firstRun) {}
}
