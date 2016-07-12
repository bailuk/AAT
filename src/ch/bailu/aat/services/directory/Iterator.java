package ch.bailu.aat.services.directory;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public abstract class Iterator implements Closeable{
    private final ServiceContext scontext;
    
    
    private Cursor cursor = null;
    private String selection="";
    
    
    public Iterator (ServiceContext sc, String s) {
       scontext = sc;
       selection=s;

       query();
       
       AppBroadcaster.register(sc.getContext(), onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);
    }

    
    private BroadcastReceiver  onSyncChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            query();
        }
    };
    
    public boolean moveToPrevious() {
        return cursor.moveToPrevious();
    }
    
    
    public boolean moveToNext() {
        return cursor.moveToNext();
    }
    
    
    public boolean moveToPosition(int pos) {
        return cursor.moveToPosition(pos);
    }

    
    public int getCount() {
        return cursor.getCount();
    }


    public int getPosition() {
        return cursor.getPosition();
    }


    public abstract GpxInformation getInfo();
    public abstract void onCursorChanged(Cursor cursor, String fid);
    
    
    public void querry(String s) {
        selection = s;
        query();
    }
    

    
    private void query() {
        final String fileID = getInfo().getPath();
        int pos=0;
        
        if (cursor != null) {
            pos = cursor.getPosition();
            cursor.close();
        }
        cursor = scontext.getDirectoryService().query(selection);
        cursor.moveToPosition(pos);
        onCursorChanged(cursor, fileID);
        
        AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DB_CURSOR_CHANGED);
    }
    
    
    @Override
    public void close() {
        if (cursor!= null) cursor.close();
        scontext.getContext().unregisterReceiver(onSyncChanged);
    }
}
