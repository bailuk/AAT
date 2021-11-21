package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.util.sql.ResultSet;
import ch.bailu.foc.Foc;

public abstract class IteratorAbstract extends Iterator implements OnPreferencesChanged {

    private OnCursorChangedListener onCursorChangedListener = NULL_LISTENER;
    private ResultSet resultSet = null;
    private final SolidDirectoryQuery sdirectory;
    private String selection="";
    private final BroadcastReceiver onSyncChanged = objs -> query();

    private final AppContext appContext;


    public IteratorAbstract (AppContext appContext) {
        this.appContext = appContext;
        sdirectory = new SolidDirectoryQuery(appContext.getStorage(), appContext);
        sdirectory.register(this);
        appContext.getBroadcaster().register(onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);
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


    @Override
    public boolean moveToPrevious() {
        if (resultSet!=null) return resultSet.moveToPrevious();
        return false;
    }


    @Override
    public boolean moveToNext() {
        if (resultSet != null) return resultSet.moveToNext();
        return false;
    }


    @Override
    public boolean moveToPosition(int pos) {
        if (resultSet != null) return resultSet.moveToPosition(pos);
        return false;
    }


    @Override
    public int getCount() {
        if (resultSet != null) return resultSet.getCount();
        return 0;
    }


    @Override
    public int getPosition() {
        if (resultSet != null) return resultSet.getPosition();
        return -1;
    }


    @Override
    public abstract GpxInformation getInfo();

    public abstract void onCursorChanged(ResultSet resultSet, Foc directory, String fid);


    @Override
    public void query() {
        String fileOnOldPosition = "";
        int oldPosition=0;

        selection = sdirectory.createSelectionString();
        if (resultSet != null) {
            oldPosition = resultSet.getPosition();
            fileOnOldPosition = getInfo().getFile().getPath();
            resultSet.close();
        }

        resultSet = appContext.getServices().getDirectoryService().query(selection);

        if (resultSet != null) {
            resultSet.moveToPosition(oldPosition);

            onCursorChanged(resultSet, sdirectory.getValueAsFile(), fileOnOldPosition);
            onCursorChangedListener.onCursorChanged();
        }
    }


    @Override
    public void close() {
        if (resultSet!= null) resultSet.close();
        sdirectory.unregister(this);
        appContext.getBroadcaster().unregister(onSyncChanged);
        onCursorChangedListener = NULL_LISTENER;
    }
}
