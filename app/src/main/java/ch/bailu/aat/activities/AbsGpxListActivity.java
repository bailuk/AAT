package ch.bailu.aat.activities;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import java.io.File;

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
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.IteratorSimple;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.DbSynchronizerBusyIndicator;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.preferences.SolidDirectoryMenuButton;
import ch.bailu.aat.views.preferences.TitleView;
import ch.bailu.aat.views.preferences.VerticalScrollView;


public abstract class AbsGpxListActivity extends AbsDispatcher implements OnItemClickListener {

    private FileMenu                    fileMenu;
    private String                      solid_key;

    private Iterator                    iteratorSimple = Iterator.NULL;

    private SolidDirectoryQuery         sdirectory;

    private GpxListView                 listView;
    private FileControlBarLayer         fileControlBar;
    private DbSynchronizerBusyIndicator busyControl;


    public abstract void                   displayFile();
    public abstract File                   getDirectory();
    public abstract String                 getLabel();
    public abstract ContentDescription[]   getGpxListItemData();
    public abstract ContentDescription[]   getSummaryData();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdirectory = new SolidDirectoryQuery(this);
        sdirectory.setValue(getDirectory().getAbsolutePath());
        solid_key = AbsGpxListActivity.class.getSimpleName() +  "_" + sdirectory.getValueAsString();

        setContentView(new Layouter().getContentView());
        createDispatcher();
    }

    private void createDispatcher() {
        addSource(new IteratorSource.Summary(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));

    }

    @Override
    public void onResumeWithService() {

        iteratorSimple = new IteratorSimple(getServiceContext());
        listView.setIterator(getServiceContext(), iteratorSimple);
        fileControlBar.setIterator(iteratorSimple);
        listView.setSelection(sdirectory.getPosition().getValue());

        getServiceContext().getDirectoryService().rescan();

        super.onResumeWithService();
    }


    @Override
    public void onPauseWithService() {
//        sdirectory.setPosition(iteratorSimple.getPosition());

        iteratorSimple.close();
        iteratorSimple = Iterator.NULL;
        listView.setIterator(getServiceContext(), iteratorSimple);
        fileControlBar.setIterator(iteratorSimple);

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        int position =
                ((AdapterView.AdapterContextMenuInfo)menuInfo).position;

        iteratorSimple.moveToPosition(position);

        fileMenu = new FileMenu(this, new File(iteratorSimple.getInfo().getPath()));
        fileMenu.inflateWithHeader(menu);
        fileMenu.prepare(menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return fileMenu.onItemClick(item);
    }


    private class Layouter {
        private final AbsGpxListActivity acontext = AbsGpxListActivity.this;


        private final String summary_label = getString(R.string.label_summary);
        private final String filter_label = getString(R.string.label_filter);
        private final String map_label = getString(R.string.intro_map);
        private final String list_label = getString(R.string.label_list);

        private final ContentView contentView = new ContentView(acontext);
        private final MainControlBar bar = new MainControlBar(acontext);

        private final VerticalScrollView summary = new VerticalScrollView(acontext);
        private final MapViewInterface map =
                MapFactory.DEF(AbsGpxListActivity.this, solid_key).list(acontext);




        public Layouter() {
            listView = new GpxListView(AbsGpxListActivity.this, getGpxListItemData());
            listView.setOnItemClickListener(acontext);
            registerForContextMenu(listView);

            busyControl = new DbSynchronizerBusyIndicator(bar.getMenu());

            fileControlBar = new FileControlBarLayer(map.getMContext(), acontext);
            map.add(fileControlBar);

            summary.add(new TitleView(acontext, getLabel()));
            summary.add(acontext,new PathDescription(acontext), InfoID.LIST_SUMMARY);
            summary.add(new TitleView(acontext, summary_label));
            summary.addAllContent(acontext, getSummaryData(), InfoID.LIST_SUMMARY);
            summary.add(new TitleView(acontext, filter_label));
            summary.addAllFilterViews(map.getMContext());


            contentView.addView(bar);
            contentView.addView(createLayout(map, summary, bar));

            bar.add(new SolidDirectoryMenuButton(acontext, sdirectory));
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

