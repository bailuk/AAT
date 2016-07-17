package ch.bailu.aat.activities;


import java.io.File;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.FileAction;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.IteratorSimple;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.DbSynchronizerBusyIndicator;
import ch.bailu.aat.views.DirectoryLinkView;
import ch.bailu.aat.views.GpxListView;
import ch.bailu.aat.views.MainControlBar;



public abstract class AbsGpxListActivity extends AbsMenu implements OnItemClickListener {
    public static final Class<?> SERVICES[] = {
        DirectoryService.class,
        CacheService.class,
    };

    
    private Iterator                    iterator = Iterator.NULL;    
    private SolidDirectory              sdirectory;
    
    
    private GpxListView                 listView;
    private DbSynchronizerBusyIndicator busyControl;

    private LinearLayout                contentView;

    public abstract File                   getDirectory();
    public abstract void                   createHeader(ControlBar bar);
    public abstract void                   createSummaryView(LinearLayout layout);
    public abstract ContentDescription[]   getGpxListItemData();

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdirectory = new SolidDirectory(this);
        sdirectory.setValue(getDirectory().getAbsolutePath());
        
        
        
        final MainControlBar bar = new MainControlBar(this, 6);
        
        contentView=new ContentView(this);
        
        createBusyIndicator(bar);
        createHeader(bar);
        contentView.addView(bar);
        
        createSummaryView(contentView);
        createListView(contentView);
        setContentView(contentView);

        bar.addView(new View(this));
        bar.addViewIgnoreSize(new DirectoryLinkView(this, getDirectory())); 

        
    }        




    
    private void createBusyIndicator(MainControlBar layout) {
        busyControl = new DbSynchronizerBusyIndicator(layout.getMenu());
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        
        AppLog.d(this, "ping");
        iterator = new IteratorSimple(getServiceContext(), sdirectory.getSelection());
        listView.setAdapter(getServiceContext(), iterator);
        listView.setSelection(sdirectory.getPosition());
    }


    @Override
    public void onPauseWithService() {
        iterator.close();
        iterator = Iterator.NULL;
        listView.setAdapter(getServiceContext(), iterator);

        super.onPauseWithService();
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
        
        
        if (iterator != null) {
            sdirectory.setPosition(iterator.getPosition());
            iterator.close();
            iterator = null;
        }

        busyControl.close();
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            displayFileOnPosition(position);
    }


    private void displayFileOnPosition(int position) {
        sdirectory.setPosition(position);
        displayFile();
    }

    public abstract void displayFile();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (iterator != null) {
            int position = 
                    ((AdapterView.AdapterContextMenuInfo)menuInfo).position;

        
            iterator.moveToPosition(position);
            new FileAction(this, iterator).createFileMenu(menu);
        }

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return new FileAction(this, iterator).onMenuItemClick(item);
    }
    
   

}

