package ch.bailu.aat.activities;


import java.io.File;
import java.io.IOException;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.AddOverlayDialog;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.DbSynchronizerBusyIndicator;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.R;



public abstract class AbsGpxListActivity extends AbsMenu implements OnItemClickListener {
    public static final Class<?> SERVICES[] = {
        DirectoryService.class,
        CacheService.class,
    };
    

    private GpxListView                 listView;
    private DbSynchronizerBusyIndicator busyIndicator;

    private LinearLayout                contentView;
    private DirectoryServiceHelper      directoryServiceHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView=new ContentView(this);

        LinearLayout header = createHeader(contentView);

        createBusyIndicator(header);
        setContentView(contentView);

        connectToServices(SERVICES);
    }        


 

 

    public abstract LinearLayout createHeader(LinearLayout contentView);


    private void createBusyIndicator(LinearLayout layout) {
        busyIndicator = new DbSynchronizerBusyIndicator(this);
        layout.addView(busyIndicator);
    }




    @Override
    public void onServicesUp() {
        try {
            directoryServiceHelper = createDirectoryServiceHelper(getDirectoryService());


            createSummaryView(contentView, getDirectoryService());
            createListView(contentView, getDirectoryService(), getCacheService());

            listView.setSelection(getDirectoryService().getStoredPosition());

        } catch (Exception e) {
            AppLog.e(this, e);
            e.printStackTrace();
        } 
    }




    public abstract DirectoryServiceHelper 
    createDirectoryServiceHelper(DirectoryService directory) throws IOException;

    public abstract void createSummaryView(LinearLayout layout, DirectoryService directory);


    private void createListView(LinearLayout contentView, DirectoryService directory, 
            CacheService loader) {

        ContentDescription data[] = getGpxListItemData();

        listView = new GpxListView(this, 
                directory, 
                loader,
                data);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        contentView.addView(listView);

    }


    public abstract ContentDescription[] getGpxListItemData();


    @Override
    public void onDestroy() {
        // busyIndicator.cleanUp();


        if (listView != null) {
            try {
                getDirectoryService().storePosition(listView.getFirstVisiblePosition());
            } catch (ServiceNotUpException e) {
                AppLog.e(this, e);
            }
            //listView.close();
        }
        if (directoryServiceHelper != null) directoryServiceHelper.close();

        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        //AppLog.d(this, "onResume()");
        if (listView != null) {
            try {
                listView.setSelection(getDirectoryService().getStoredPosition());
            } catch (ServiceNotUpException e) {
                AppLog.e(this, e);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        try {
            displayFileOnPosition(position);

        } catch (ServiceNotUpException e) {
            AppLog.e(this, e);
        }
    }


    private void displayFileOnPosition(int position) throws ServiceNotUpException {
        getDirectoryService().setPosition(position);
        displayFile();
    }

    public abstract void displayFile();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        try {
            int position = 
                    ((AdapterView.AdapterContextMenuInfo)menuInfo).position;

            displayContextMenu(getDirectoryService(), menu, position);

        } catch (ServiceNotUpException e) {
            AppLog.e(this, e);
            e.printStackTrace();
        }
    }


    private void displayContextMenu(DirectoryService directory, ContextMenu menu, int position) {

        directory.setPosition(position);

        getMenuInflater().inflate(R.menu.contextmenu, menu);
        menu.setHeaderTitle(directory.getCurrent().getName());
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            if (item.getItemId()== R.id.m_file_delete) {
                directoryServiceHelper.deleteSelectedFile(this);

            } else if (item.getItemId() == R.id.m_file_reload) {
                directoryServiceHelper.refreshSelectedEntry();

            } else if (item.getItemId() == R.id.m_file_rename) {
                directoryServiceHelper.renameSelectedFile(this);

            } else if (item.getItemId() == R.id.m_file_overlay) {
                new AddOverlayDialog(this,
                        new File(getDirectoryService().getCurrent().getPath()));

            } else if (item.getItemId() == R.id.m_file_mock) {
                SolidMockLocationFile smock = new SolidMockLocationFile(this);
                smock.setValue(getDirectoryService().getCurrent().getPath());
            }

        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return true;
    }
}

