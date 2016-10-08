package ch.bailu.aat.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.OnContentUpdatedInterface;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.IteratorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.RootDispatcher;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.TrackDescriptionView;
import ch.bailu.aat.views.description.VerticalView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.control.EditorOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;
import ch.bailu.aat.views.preferences.VerticalScrollView;

@SuppressLint("Registered")
public class AbsFileContentActivity extends AbsDispatcher implements OnClickListener {

    protected IteratorSource  currentFile;
    protected ImageButton nextView, nextFile, previousFile, fileOperation;

    private boolean            firstRun = true;

    private BusyButton         busyButton;
    private MultiView          multiView;
    protected OsmInteractiveView map;

    protected EditorHelper editor_helper;
    protected EditorSource editor_source;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstRun = true;
    }

    protected void createViews(final String SOLID_KEY) {
        final ViewGroup contentView = new ContentView(this);

        multiView = createMultiView(SOLID_KEY);
        contentView.addView(createButtonBar());
        contentView.addView(multiView);

        setContentView(contentView);
    }


    private ControlBar createButtonBar() {
        MainControlBar bar = new MainControlBar(getServiceContext());

        nextView = bar.addImageButton(R.drawable.go_next_inverse);
        previousFile =  bar.addImageButton(R.drawable.go_up_inverse);
        nextFile = bar.addImageButton(R.drawable.go_down_inverse);
        fileOperation = bar.addImageButton(R.drawable.edit_select_all_inverse);

        ToolTip.set(fileOperation, R.string.tt_menu_file);

        busyButton = bar.getMenu();
        busyButton.startWaiting();

        bar.setOrientation(AppLayout.getOrientationAlongSmallSide(this));
        bar.setOnClickListener1(this);
        return bar;
    }


    protected MultiView createMultiView(final String SOLID_KEY) {
        map = new OsmInteractiveView(getServiceContext(), SOLID_KEY);

        final OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_TRACKER),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_FILEVIEW),
                new CurrentLocationOverlay(map),
                new GridDynOverlay(map, getServiceContext()),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),
                new EditorOverlay(
                        map,
                        getServiceContext(),
                        GpxInformation.ID.INFO_ID_EDITOR_DRAFT,
                        editor_helper),

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

        VerticalScrollView summary = new VerticalScrollView(this);
        VerticalView graph = new VerticalView(this, SOLID_KEY,
                GpxInformation.ID.INFO_ID_FILEVIEW,
                new TrackDescriptionView[] {
                        new DistanceAltitudeGraphView(this, SOLID_KEY),
                        new DistanceSpeedGraphView(this, SOLID_KEY)});


        MultiView mv = new MultiView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_ALL);
        mv.add(summary, summary.addAllContent(summaryData, GpxInformation.ID.INFO_ID_FILEVIEW));
        mv.addT(map);
        mv.addT(graph);
        return mv;
    }





    protected void createDispatcher() {
        currentFile = new IteratorSource.FollowFile(getServiceContext());

        final OnContentUpdatedInterface[] target = new OnContentUpdatedInterface[] {
                multiView, this, busyButton.getBusyControl(GpxInformation.ID.INFO_ID_FILEVIEW)
        };



        editor_source = new EditorSource(getServiceContext(), editor_helper);

        ContentSource[] source = new ContentSource[] {
                editor_source,
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext()),
                new OverlaySource(getServiceContext()),
                currentFile
        };

        setDispatcher(new RootDispatcher(this,source, target));
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();

        if (firstRun) {
            frameCurrentFile();
            firstRun = false;
        }
    }


    private void frameCurrentFile() {
        map.frameBoundingBox(currentFile.getInfo().getBoundingBox());
    }



    @Override
    public void onClick(View v) {
        if (v == nextView) {
            multiView.setNext();

        } else if (v == previousFile) {
            switchFile(v);

        } else if (v ==nextFile) {
            switchFile(v);

        } else if (v == fileOperation) {
            new FileMenu(currentFile.fileAction(this)).showAsPopup(this, v);
        }

    }

    protected void switchFile(View v) {
        busyButton.startWaiting();

        if (v==nextFile)
            currentFile.moveToNext();
        else if (v==previousFile)
            currentFile.moveToPrevious();

        frameCurrentFile();
    }
}
