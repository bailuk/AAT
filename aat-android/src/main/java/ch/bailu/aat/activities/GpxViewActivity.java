package ch.bailu.aat.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource;
import ch.bailu.aat_lib.dispatcher.CustomFileSource;
import ch.bailu.aat_lib.dispatcher.OverlaySource;
import ch.bailu.aat_lib.dispatcher.TrackerSource;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.To;
import ch.bailu.aat.menus.ContentMenu;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.BusyViewControlIID;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.graph.GraphViewFactory;
import ch.bailu.aat.views.preferences.VerticalScrollView;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class GpxViewActivity extends ActivityContext
        implements OnClickListener, OnContentUpdatedInterface {

    private static final String SOLID_KEY=GpxViewActivity.class.getSimpleName();



    private ImageButtonViewGroup fileOperation, copyTo;
    private BusyViewControlIID busyControl;
    private MapViewInterface   map;

    private String fileID;
    private Foc content;

    private final UiTheme theme = AppTheme.trackContent;

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

            try {
                content = FocAndroid.factory(this, uri.toString());
                fileID = content.getPath();


                final ContentView contentView = new ContentView(this, theme);

                MainControlBar bar = new MainControlBar(this);

                contentView.add(bar);
                contentView.add(getErrorView());

                View view = createLayout(bar, contentView);
                initButtonBar(bar);

                contentView.add(view);

                busyControl = new BusyViewControlIID(contentView);

                setContentView(contentView);
                createDispatcher();
            } catch (Exception e) {
                AppLog.e(this, e);
            }
        }


    }



    private View createLayout(MainControlBar bar, ContentView contentView) {
        map = MapFactory.DEF(this, SOLID_KEY).externalContent();


        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this,
                FileContentActivity.getSummaryData(this), theme, InfoID.FILEVIEW);

        View graph = GraphViewFactory.all(getAppContext(),this, this, theme, InfoID.FILEVIEW);

        if (AppLayout.isTablet(this)) {
            return createPercentageLayout(summary, graph);
        } else {
            return createMultiView(bar, summary, graph, contentView);
        }

    }

    protected View createMultiView(MainControlBar bar,
                                   View summary, View graph,
                                   ContentView contentView) {

        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(summary);
        mv.add(To.view(map));
        mv.add(graph);

        bar.addMvNext(mv);

        contentView.addMvIndicator(mv);
        return mv;
    }


    private View createPercentageLayout(
            View summary, View graph) {

        PercentageLayout a = new PercentageLayout(this);
        a.setOrientation(AppLayout.getOrientationAlongLargeSide(this));
        a.add(To.view(map), 60);
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


        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setOnClickListener1(this);
        return bar;
    }



    private void createDispatcher() {
        addSource(new TrackerSource(getServiceContext(),getBroadcaster()));
        addSource(new CurrentLocationSource(getServiceContext(),getBroadcaster()));
        addSource(new OverlaySource(getAppContext()));
        addSource(new CustomFileSource(getAppContext(), fileID));

        addTarget(this, InfoID.FILEVIEW);
        addTarget(busyControl, InfoID.FILEVIEW,
                InfoID.OVERLAY,
                InfoID.OVERLAY+1,
                InfoID.OVERLAY+2,
                InfoID.OVERLAY+3);
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
