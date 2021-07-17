package ch.bailu.aat.services.directory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.Foc;

public abstract class IteratorAbstract extends Iterator implements OnPreferencesChanged {
    private final ServiceContext scontext;


    private OnCursorChangedListener onCursorChangedListener = NULL_LISTENER;
    private Cursor cursor = null;
    private final SolidDirectoryQuery sdirectory;
    private String selection="";


    public IteratorAbstract (ServiceContext sc) {
        sdirectory = new SolidDirectoryQuery(new Storage(sc.getContext()), new AndroidFocFactory(sc.getContext()));
        scontext = sc;
        sdirectory.register(this);
        OldAppBroadcaster.register(sc.getContext(), onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);
    }


    @Override
    public void setOnCursorChangedLinsener(OnCursorChangedListener l) {
        onCursorChangedListener = l;
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
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
