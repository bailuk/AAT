package ch.bailu.aat.activities;

import android.content.Intent;
import android.net.Uri;
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
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.menus.ContentMenu;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.preferences.VerticalScrollView;
import ch.bailu.simpleio.foc.Foc;

public class GpxViewActivity extends AbsDispatcher
        implements OnClickListener, OnContentUpdatedInterface {

    private static final String SOLID_KEY=GpxViewActivity.class.getSimpleName();



    private ImageButton        fileOperation, copyTo;
    private BusyButton         busyButton;
    private MapViewInterface   map;

    private String fileID;
    private Foc content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri==null) {

            if (intent.hasExtra(Intent.EXTRA_STREAM)) {
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            }
        }




        if (uri != null) {
            content = FocAndroid.factory(this, uri.toString());

            fileID = content.toString();

            final LinearLayout contentView = new ContentView(this);

            MainControlBar bar = new MainControlBar(this);

            contentView.addView(bar);
            View view = createLayout(bar);
            initButtonBar(bar);

            contentView.addView(view);
            setContentView(contentView);
            createDispatcher();
        }


    }


    private View createLayout(MainControlBar bar) {
        map = MapFactory.DEF(this, SOLID_KEY).externalContent();


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


        if (AppLayout.isTablet(this)) {
            return createPercentageLayout(summary, graph);
        } else {
            return createMultiView(bar, summary, graph);
        }

    }

    protected View createMultiView(MainControlBar bar,
                                   View summary, View graph) {

        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(summary);
        mv.add(map.toView());
        mv.add(graph);

        bar.addMvNext(mv);
        return mv;
    }


    private View createPercentageLayout(
            View summary, View graph) {

        PercentageLayout a = new PercentageLayout(this);
        a.setOrientation(AppLayout.getOrientationAlongLargeSide(this));
        a.add(map.toView(), 60);
        a.add(summary, 40);

        PercentageLayout b = new PercentageLayout(this);
        b.add(a, 80);
        b.add(graph, 20);

        return b;
    }

    private ControlBar initButtonBar(MainControlBar bar) {


        copyTo = bar.addImageButton(R.drawable.document_save_as_inverse);

        fileOperation = bar.addImageButton(R.drawable.edit_select_all_inverse);

        ToolTip.set(copyTo, R.string.file_copy);
        ToolTip.set(fileOperation, R.string.tt_menu_file);

        busyButton = bar.getMenu();
        busyButton.startWaiting();

        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setOnClickListener1(this);
        return bar;
    }



    private void createDispatcher() {
        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
        addSource(new CustomFileSource(getServiceContext(), fileID));

        addTarget(this, InfoID.FILEVIEW);
        addTarget(busyButton.getBusyControl(InfoID.FILEVIEW), InfoID.FILEVIEW);
    }




    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        map.frameBounding(info.getBoundingBox());
    }


    @Override
    public void onClick(View v) {
        if (v == copyTo && content != null) {
            FileAction.copyToDir(this, content);

        } else if (v == fileOperation && content != null) {

            new ContentMenu(getServiceContext(), content).showAsPopup(this, fileOperation);
        }

    }
}
