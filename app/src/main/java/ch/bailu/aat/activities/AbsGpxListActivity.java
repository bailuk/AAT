package ch.bailu.aat.activities;


import java.io.File;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.IteratorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.helpers.FileAction;
import ch.bailu.aat.helpers.file.FileIntent;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.IteratorSimple;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.DbSynchronizerBusyIndicator;
import ch.bailu.aat.views.DirectorySelection;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.SummaryListView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.VerticalLayoutView;
import ch.bailu.aat.views.ViewWrapper;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.control.FileControlBar;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;



public abstract class AbsGpxListActivity extends AbsDispatcher implements OnItemClickListener, OnClickListener {

    private FileMenu fileMenu;
    private String                      solid_key;

    private Iterator                    iteratorSimple = Iterator.NULL;

    private SolidDirectory              sdirectory;

    private final ImageButton[]         selectView = new ImageButton[3];
    private ImageButton fileManager;
    private MultiView                   multiView;


    private GpxListView                 listView;
    private DbSynchronizerBusyIndicator busyControl;


    public abstract void                   displayFile();
    public abstract File                   getDirectory();
    public abstract String                 getLabel();
    public abstract ContentDescription[]   getGpxListItemData();
    public abstract ContentDescription[]   getSummaryData();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdirectory = new SolidDirectory(this);
        sdirectory.setValue(getDirectory().getAbsolutePath());
        solid_key = AbsGpxListActivity.class.getSimpleName() +  "_" + sdirectory.getValue();


        final LinearLayout contentView = new ContentView(this);
        contentView.addView(createControlBar());
        contentView.addView(createMultiView());
        setContentView(contentView);

        selectView(multiView.getActive());
        createDispatcher();
    }


    private ControlBar createControlBar() {
        final MainControlBar bar = new MainControlBar(getServiceContext(), 4);

        busyControl = new DbSynchronizerBusyIndicator(bar.getMenu());
        for (int i = 0; i< selectView.length; i++) {
            selectView[i] = bar.addImageButton(R.drawable.radio_inverse);
        }
        fileManager = bar.addImageButton(R.drawable.folder_inverse);
        bar.setOnClickListener1(this);

        return bar;
    }


    private MultiView createMultiView() {
        final OsmInteractiveView map = new OsmInteractiveView(getServiceContext(), solid_key);

        final OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_LIST_SUMMARY),
                new FileControlBar(map, this),
                new CurrentLocationOverlay(map),
                new GridDynOverlay(map, getServiceContext()),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),
        };
        map.setOverlayList(overlayList);


        ContentDescription summaryData[] = getSummaryData();

        ContentDescription listData[] = getGpxListItemData();
        listView = new GpxListView(this, listData);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        final TextView label = new TextView(this);
        label.setText(getLabel());
        AppTheme.themify(label);
        label.setTextColor(AppTheme.getHighlightColor());

        final TrackDescriptionView filterLayout[] = {
                new ViewWrapper(label),
                new ViewWrapper(new DirectorySelection(map.map)),
                new SummaryListView(
                        this, solid_key, GpxInformation.ID.INFO_ID_LIST_SUMMARY, summaryData),
        };


        final TrackDescriptionView multiViewLayout[] = {
                new ViewWrapper(listView),
                map,
                new VerticalLayoutView(this, solid_key, GpxInformation.ID.INFO_ID_ALL, filterLayout),
        };


        multiView = new MultiView(this, solid_key, GpxInformation.ID.INFO_ID_ALL, multiViewLayout);
        return multiView;
    }




    private void createDispatcher() {


        final DescriptionInterface[] target = new DescriptionInterface[] {
                multiView, this
        };

        final IteratorSource summary = new IteratorSource.Summary(getServiceContext());

        ContentSource[] source = new ContentSource[] {
                new OverlaySource(getServiceContext()),
                new CurrentLocationSource(getServiceContext()),
                summary
        };

        setDispatcher(new ContentDispatcher(this,source, target));

    }




    @Override
    public void onResumeWithServices() {
        iteratorSimple = new IteratorSimple(getServiceContext());
        listView.setAdapter(getServiceContext(), iteratorSimple);
        listView.setSelection(sdirectory.getPosition().getValue());

        super.onResumeWithServices();
    }


    @Override
    public void onPauseWithService() {
        //sdirectory.setPosition(iteratorSimple.getPosition());

        iteratorSimple.close();
        iteratorSimple = Iterator.NULL;
        listView.setAdapter(getServiceContext(), iteratorSimple);

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

        fileMenu = new FileMenu(new FileAction(this, iteratorSimple));
        fileMenu.inflateWithHeader(menu);
        fileMenu.prepare(menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return fileMenu.onItemClick(item);
    }


    @Override
    public void onClick(View v) {
        if (v == fileManager) {
            File directory = new File(sdirectory.getValue());
            new FileIntent(directory).view(this);
        } else {
            for (int i = 0; i < selectView.length; i++) {
                if (v == selectView[i]) {
                    setView(i);
                }
            }
        }

    }

    public void setView(int i) {
        multiView.setActive(i);
        selectView(i);
    }

    private void selectView(int s) {
        for (int i=0; i<selectView.length; i++) {
            if (i==s) selectView[i].setImageResource(R.drawable.radio_checked_inverse);
            else selectView[i].setImageResource(R.drawable.radio_inverse);
        }
    }
}

