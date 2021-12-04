package ch.bailu.aat.activities;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.IteratorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.To;
import ch.bailu.aat.map.layer.control.FileControlBarLayer;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.service.directory.Iterator;
import ch.bailu.aat_lib.service.directory.IteratorSimple;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.BusyViewControlDbSync;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.preferences.TitleView;
import ch.bailu.aat.views.preferences.VerticalScrollView;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.PathDescription;
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.Foc;


public abstract class AbsGpxListActivity extends ActivityContext implements OnItemClickListener, OnPreferencesChanged {

    private String                      solid_key;

    private Iterator                    iteratorSimple = Iterator.NULL;

    private SolidDirectoryQuery         sdirectory;

    private GpxListView                 listView;
    private FileControlBarLayer         fileControlBar;
    private BusyViewControlDbSync       busyControl;


    public abstract void                   displayFile();
    public abstract Foc                    getDirectory();
    public abstract String                 getLabel();
    public abstract ContentDescription[]   getGpxListItemData();
    public abstract ContentDescription[]   getSummaryData();

    protected final UiTheme theme = AppTheme.trackList;
    protected final UiTheme filterTheme = AppTheme.filter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdirectory = new SolidDirectoryQuery(new Storage(this), new AndroidFocFactory(this));
        sdirectory.setValue(getDirectory().getPath());
        solid_key = AbsGpxListActivity.class.getSimpleName() +  "_" + sdirectory.getValueAsString();

        setContentView(new Layouter().getContentView());
        createDispatcher();
    }

    private void createDispatcher() {
        addSource(new IteratorSource.Summary(getAppContext()));
        addSource(new OverlaySource(getAppContext()));
        addSource(new CurrentLocationSource(getServiceContext(),getBroadcaster()));

        addTarget(busyControl, InfoID.OVERLAY, InfoID.OVERLAY+1,InfoID.OVERLAY+2,InfoID.OVERLAY+3);

    }


    @Override
    public void onResumeWithService() {

        iteratorSimple = new IteratorSimple(getAppContext());
        listView.setIterator(this, iteratorSimple);
        fileControlBar.setIterator(iteratorSimple);
        listView.setSelection(sdirectory.getPosition().getValue());

        getServiceContext().getDirectoryService().rescan();

        sdirectory.register(this);
        setListBackgroundColor();

        super.onResumeWithService();
    }


    @Override
    public void onPauseWithService() {
        iteratorSimple.close();
        iteratorSimple = Iterator.NULL;
        listView.setIterator(this, iteratorSimple);
        fileControlBar.setIterator(iteratorSimple);

        sdirectory.unregister(this);
        super.onPauseWithService();
    }


    @Override
    public void onDestroy() {
        busyControl.close();

        super.onDestroy();
    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        displayFileOnPosition(position);
    }


    private void displayFileOnPosition(int position) {
        sdirectory.getPosition().setValue(position);
        displayFile();
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (sdirectory.containsKey(key)) {
            setListBackgroundColor();
        }
    }

    private void setListBackgroundColor() {
        if (sdirectory.createSelectionString().length() > 0) {
            listView.themify(filterTheme);

        } else {
            listView.themify(theme);
        }
    }


    private class Layouter {
        private final AbsGpxListActivity acontext = AbsGpxListActivity.this;


        private final String summary_label = getString(R.string.label_summary);
        private final String filter_label = getString(R.string.label_filter);
        private final String map_label = getString(R.string.intro_map);
        private final String list_label = getString(R.string.label_list);

        private final ContentView contentView = new ContentView(acontext, theme);


        public Layouter() {
            listView = new GpxListView(AbsGpxListActivity.this, getGpxListItemData());
            listView.setOnItemClickListener(acontext);
            registerForContextMenu(listView);

            busyControl = new BusyViewControlDbSync(contentView);

            MapViewInterface map = MapFactory.DEF(AbsGpxListActivity.this, solid_key).list(acontext);
            fileControlBar = new FileControlBarLayer(getServiceContext(), map.getMContext(), acontext, getAppContext().getSummaryConfig());
            map.add(fileControlBar);

            VerticalScrollView summary = new VerticalScrollView(acontext);
            summary.add(new TitleView(acontext, getLabel(), theme));
            summary.add(acontext,new PathDescription(), theme, InfoID.LIST_SUMMARY);
            summary.add(new TitleView(acontext, summary_label, theme));
            summary.addAllContent(acontext, getSummaryData(), theme, InfoID.LIST_SUMMARY);

            TitleView title = new TitleView(acontext, filter_label, filterTheme);
            //AppTheme.alt.background(title);

            summary.add(title);
            summary.addAllFilterViews(map.getMContext(), filterTheme);


            MainControlBar bar = new MainControlBar(acontext);
            View layout = createLayout(map, summary, bar);



            contentView.add(bar);
            contentView.add(getErrorView());
            contentView.add(layout);

        }


        private View createLayout(MapViewInterface map,
                                  VerticalScrollView summary,
                                  MainControlBar bar) {
            if (AppLayout.isTablet(acontext)) {
                return createTabletLayout(map, summary);
            } else {
                return createMvLayout(map, summary, bar);
            }
        }

        private View createMvLayout(MapViewInterface map,
                                    VerticalScrollView summary, MainControlBar bar) {

            MultiView multiView = new MultiView(acontext, solid_key);

            multiView.add(listView, list_label);
            multiView.add(To.view(map), map_label);
            multiView.add(summary, summary_label + "/" + filter_label);
            bar.addAll(multiView);
            contentView.addMvIndicator(multiView);


            return multiView;
        }

        private View createTabletLayout(MapViewInterface map,
                                        VerticalScrollView summary) {

            if (AppLayout.getOrientation(acontext)== Configuration.ORIENTATION_LANDSCAPE) {
                PercentageLayout a = new PercentageLayout(acontext);
                a.setOrientation(LinearLayout.HORIZONTAL);
                a.add(listView,30);
                a.add(summary,30);
                a.add(To.view(map), 40);
                return a;
            } else {
                PercentageLayout a = new PercentageLayout(acontext);
                a.setOrientation(LinearLayout.HORIZONTAL);
                a.add(listView, 50);
                a.add(summary, 50);

                PercentageLayout b = new PercentageLayout(acontext);
                b.add(a, 60);
                b.add(To.view(map), 40);
                return b;
            }
        }

        public ContentView getContentView() {
            return contentView;
        }
    }
}

