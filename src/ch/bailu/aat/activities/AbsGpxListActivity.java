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
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.helpers.AppFile;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.AddOverlayDialog;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.DbSynchronizerBusyIndicator;
import ch.bailu.aat.views.GpxListView;



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

        createSummaryView(contentView);
        createListView(contentView);
        setContentView(contentView);

    }        


    public abstract LinearLayout createHeader(LinearLayout contentView);


    private void createBusyIndicator(LinearLayout layout) {
        busyIndicator = new DbSynchronizerBusyIndicator(this);
        layout.addView(busyIndicator);
    }





    @Override
    public void onServicesUp() {
        try {
            directoryServiceHelper = createDirectoryServiceHelper();
            listView.setAdapter(getServiceContext());
            listView.setSelection(getServiceContext().getDirectoryService().getStoredPosition());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public abstract DirectoryServiceHelper createDirectoryServiceHelper() throws IOException;

    public abstract void createSummaryView(LinearLayout layout);


    private void createListView(LinearLayout contentView) {

        ContentDescription data[] = getGpxListItemData();

        listView = new GpxListView(this, data);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        contentView.addView(listView);

    }


    public abstract ContentDescription[] getGpxListItemData();


    @Override
    public void onDestroy() {


        getServiceContext().getDirectoryService().storePosition(listView.getFirstVisiblePosition());
        if (directoryServiceHelper != null) directoryServiceHelper.close();

        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        //listView.setSelection(getServiceContext().getDirectoryService().getStoredPosition());
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
                directoryServiceHelper.deleteSelectedFile(this);

            } else if (item.getItemId() == R.id.m_file_reload) {
                directoryServiceHelper.refreshSelectedEntry();

            } else if (item.getItemId() == R.id.m_file_rename) {
                directoryServiceHelper.renameSelectedFile(this);

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

