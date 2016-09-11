package ch.bailu.aat.services.directory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.services.ServiceContext;

public abstract class IteratorAbstract extends Iterator implements OnSharedPreferenceChangeListener {
    private final ServiceContext scontext;

   
    private OnCursorChangedListener onCursorChangedListener = NULL_LISTENER;
    private Cursor cursor = null;
    private final SolidDirectory sdirectory;
    private String selection="";
    

    public IteratorAbstract (ServiceContext sc) {
        sdirectory = new SolidDirectory(sc.getContext());
        scontext = sc;
        sdirectory.register(this);
        AppBroadcaster.register(sc.getContext(), onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);
    }

    
    @Override
    public void setOnCursorChangedLinsener(OnCursorChangedListener l) {
        onCursorChangedListener = l;
    }
    
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
        if (sdirectory.containsKey(key) && selection.equals(sdirectory.createSelectionString()) == false) {
            query();
        }
    }

    
    private BroadcastReceiver  onSyncChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            query();
        }
    };

    
    @Override
    public boolean moveToPrevious() {
        if (cursor!=null) return cursor.moveToPrevious();
        return false;
    }


    @Override
    public boolean moveToNext() {
        if (cursor != null) return cursor.moveToNext();
        return false;
    }


    @Override
    public boolean moveToPosition(int pos) {
        if (cursor != null) return cursor.moveToPosition(pos);
        return false;
    }


    @Override
    public int getCount() {
        if (cursor != null) return cursor.getCount();
        return 0;
    }


    @Override
    public int getPosition() {
        if (cursor != null) return cursor.getPosition();
        return -1;
    }


    @Override
    public abstract GpxInformation getInfo();

    public abstract void onCursorChanged(Cursor cursor, String fid);


    @Override
    public void query() {
        final String fileOnOldPosition = getInfo().getPath();
        int oldPosition=0;

        selection = sdirectory.createSelectionString();
        AppLog.d(this, selection);
        if (cursor != null) {
            oldPosition = cursor.getPosition();
            cursor.close();
        }
        cursor = scontext.getDirectoryService().query(selection);
        cursor.moveToPosition(oldPosition);

        onCursorChanged(cursor, fileOnOldPosition);
        onCursorChangedListener.onCursorChanged();
    }


    @Override
    public void close() {
        if (cursor!= null) cursor.close();
        scontext.getContext().unregisterReceiver(onSyncChanged);
        onCursorChangedListener = NULL_LISTENER;
    }
}
