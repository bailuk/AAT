package ch.bailu.aat.activities;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.IteratorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.layer.control.FileControlBarLayer;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.IteratorSimple;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.BusyViewControlDbSync;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.preferences.TitleView;
import ch.bailu.aat.views.preferences.VerticalScrollView;
import ch.bailu.util_java.foc.Foc;


public abstract class AbsGpxListActivity extends ActivityContext implements OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdirectory = new SolidDirectoryQuery(this);
        sdirectory.setValue(getDirectory().getPath());
        solid_key = AbsGpxListActivity.class.getSimpleName() +  "_" + sdirectory.getValueAsString();

        setContentView(new Layouter().getContentView());
        createDispatcher();
    }

    private void createDispatcher() {
        addSource(new IteratorSource.Summary(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));

        addTarget(busyControl, InfoID.OVERLAY, InfoID.OVERLAY+1,InfoID.OVERLAY+2,InfoID.OVERLAY+3);

    }

    @Override
    public void onResumeWithService() {

        iteratorSimple = new IteratorSimple(getServiceContext());
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (sdirectory.containsKey(s)) {
            setListBackgroundColor();
        }
    }

    private void setListBackgroundColor() {
        if (sdirectory.createSelectionString().length() > 0) {
            AppTheme.alt.background(listView);
            //listView.setBackgroundColor(AppTheme.getAltBackgroundColor());
        } else {
            AppTheme.main.background(listView);//listView.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    private class Layouter {
        private final AbsGpxListActivity acontext = AbsGpxListActivity.this;


        private final String summary_label = getString(R.string.label_summary);
        private final String filter_label = getString(R.string.label_filter);
        private final String map_label = getString(R.string.intro_map);
        private final String list_label = getString(R.string.label_list);

        private final ContentView contentView = new ContentView(acontext);


        public Layouter() {
            listView = new GpxListView(AbsGpxListActivity.this, getGpxListItemData());
            listView.setOnItemClickListener(acontext);
            registerForContextMenu(listView);

            busyControl = new BusyViewControlDbSync(contentView);

            MapViewInterface map = MapFactory.DEF(AbsGpxListActivity.this, solid_key).list(acontext);
            fileControlBar = new FileControlBarLayer(map.getMContext(), acontext);
            map.add(fileControlBar);

            VerticalScrollView summary = new VerticalScrollView(acontext);
            summary.add(new TitleView(acontext, getLabel()));
            summary.add(acontext,new PathDescription(acontext), InfoID.LIST_SUMMARY);
            summary.add(new TitleView(acontext, summary_label));
            summary.addAllContent(acontext, getSummaryData(), InfoID.LIST_SUMMARY);

            TitleView title = new TitleView(acontext, filter_label);
            AppTheme.alt.background(title);

            summary.add(title);
            summary.addAllFilterViews(map.getMContext());


            MainControlBar bar = new MainControlBar(acontext);
            contentView.add(bar);
            contentView.add(createLayout(map, summary, bar));

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
            multiView.add(map.toView(), map_label);
            multiView.add(summary, summary_label + "/" + filter_label);
            bar.addAll(multiView);

            return multiView;
        }

        private View createTabletLayout(MapViewInterface map,
                                        VerticalScrollView summary) {

            if (AppLayout.getOrientation(acontext)== Configuration.ORIENTATION_LANDSCAPE) {
                PercentageLayout a = new PercentageLayout(acontext);
                a.setOrientation(LinearLayout.HORIZONTAL);
                a.add(listView,30);
                a.add(summary,30);
                a.add(map.toView(), 40);
                return a;
            } else {
                PercentageLayout a = new PercentageLayout(acontext);
                a.setOrientation(LinearLayout.HORIZONTAL);
                a.add(listView, 50);
                a.add(summary, 50);

                PercentageLayout b = new PercentageLayout(acontext);
                b.add(a, 60);
                b.add(map.toView(), 40);
                return b;
            }
        }

        public ContentView getContentView() {
            return contentView;
        }
    }
}

