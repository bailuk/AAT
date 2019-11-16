package ch.bailu.aat.services.directory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.util_java.foc.Foc;

public abstract class IteratorAbstract extends Iterator implements OnSharedPreferenceChangeListener {
    private final ServiceContext scontext;


    private OnCursorChangedListener onCursorChangedListener = NULL_LISTENER;
    private Cursor cursor = null;
    private final SolidDirectoryQuery sdirectory;
    private String selection="";


    public IteratorAbstract (ServiceContext sc) {
        sdirectory = new SolidDirectoryQuery(sc.getContext());
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


    private final BroadcastReceiver  onSyncChanged = new BroadcastReceiver () {
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

    public abstract void onCursorChanged(Cursor cursor, Foc directory, String fid);


    @Override
    public void query() {
        String fileOnOldPosition = "";
        int oldPosition=0;

        selection = sdirectory.createSelectionString();
        if (cursor != null) {
            oldPosition = cursor.getPosition();
            fileOnOldPosition = getInfo().getFile().getPath();
            cursor.close();
        }

        cursor = scontext.getDirectoryService().query(selection);

        if (cursor != null) {
            cursor.moveToPosition(oldPosition);

            onCursorChanged(cursor, sdirectory.getValueAsFile(), fileOnOldPosition);
            onCursorChangedListener.onCursorChanged();
        }
    }


    @Override
    public void close() {
        if (cursor!= null) cursor.close();
        sdirectory.unregister(this);
        scontext.getContext().unregisterReceiver(onSyncChanged);
        onCursorChangedListener = NULL_LISTENER;
    }
}
