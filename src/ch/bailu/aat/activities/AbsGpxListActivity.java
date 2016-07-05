package ch.bailu.aat.activities;


import java.io.File;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.helpers.AppFile;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.AddOverlayDialog;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.DbSynchronizerBusyIndicator;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.views.MainControlBar;



public abstract class AbsGpxListActivity extends AbsMenu implements OnItemClickListener {
    public static final Class<?> SERVICES[] = {
        DirectoryService.class,
        CacheService.class,
    };


    private GpxListView                 listView;
    private DbSynchronizerBusyIndicator busyControl;

    private LinearLayout                contentView;
    private DirectoryServiceHelper      directory;


    public abstract void                   createHeader(ControlBar bar);
    public abstract DirectoryServiceHelper createDirectoryServiceHelper();
    public abstract void                   createSummaryView(LinearLayout layout);
    public abstract ContentDescription[]   getGpxListItemData();

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        final MainControlBar bar = new MainControlBar(this, 6);
        
        contentView=new ContentView(this);
        
        createBusyIndicator(bar);
        createHeader(bar);
        contentView.addView(bar);
        
        createSummaryView(contentView);
        createListView(contentView);
        setContentView(contentView);

        directory = createDirectoryServiceHelper();

        final TextView v=AppTheme.getTitleTextView(this, directory.getDirectory().getAbsolutePath());
        v.setTextSize(v.getTextSize()/2);
        bar.addView(new View(this));
        bar.addViewIgnoreSize(v);

    }        




    
    private void createBusyIndicator(MainControlBar layout) {
        busyControl = new DbSynchronizerBusyIndicator(layout.getMenu());
    }


    @Override
    public void onServicesUp(boolean firstRun) {
        if (firstRun) {
            directory.reopen();
            listView.setAdapter(getServiceContext());
        }
        
        directory.rescan();
        listView.setSelection(getServiceContext().getDirectoryService().getPosition());
    }



    private void createListView(LinearLayout contentView) {

        ContentDescription data[] = getGpxListItemData();

        listView = new GpxListView(this, data);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        contentView.addView(listView);

    }




    @Override
    public void onDestroy() {
        directory.close();
        busyControl.close();
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            displayFileOnPosition(position);
    }


    private void displayFileOnPosition(int position) {
        getServiceContext().getDirectoryService().setPosition(position);
        displayFile();
    }

    public abstract void displayFile();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        int position = 
                ((AdapterView.AdapterContextMenuInfo)menuInfo).position;

        displayContextMenu(getServiceContext().getDirectoryService(), menu, position);

    }


    private void displayContextMenu(DirectoryService.Self directory, ContextMenu menu, int position) {
        directory.setPosition(position);

        getMenuInflater().inflate(R.menu.contextmenu, menu);
        menu.setHeaderTitle(directory.getCurrent().getName());
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            if (item.getItemId()== R.id.m_file_delete) {
                directory.deleteSelected(this);

            } else if (item.getItemId() == R.id.m_file_reload) {
                directory.refreshSelected();

            } else if (item.getItemId() == R.id.m_file_rename) {
                directory.renameSelectedFile(this);

            } else if (item.getItemId() == R.id.m_file_overlay) {
                new AddOverlayDialog(this,
                        new File(getServiceContext().getDirectoryService().getCurrent().getPath()));

            } else if (item.getItemId() == R.id.m_file_mock) {
                SolidMockLocationFile smock = new SolidMockLocationFile(this);
                smock.setValue(getServiceContext().getDirectoryService().getCurrent().getPath());

            } else if (item.getItemId() == R.id.m_file_send) {
                AppFile.send(this, new File(getServiceContext().getDirectoryService().getCurrent().getPath()));

            } else if (item.getItemId() == R.id.m_file_copy) {
                AppFile.copyTo(this, new File(getServiceContext().getDirectoryService().getCurrent().getPath()));

            }

        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return true;
    }
}

