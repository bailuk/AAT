package ch.bailu.aat_lib.service.directory;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public abstract class IteratorAbstract extends Iterator implements OnPreferencesChanged {

    private OnCursorChangedListener onCursorChangedListener = NULL_LISTENER;
    private DbResultSet resultSet = null;
    private final SolidDirectoryQuery sdirectory;
    private String selection="";
    private final BroadcastReceiver onSyncChanged = objs -> query();

    private final AppContext appContext;


    public IteratorAbstract (AppContext appContext) {
        this.appContext = appContext;
        sdirectory = new SolidDirectoryQuery(appContext.getStorage(), appContext);
        sdirectory.register(this);
        appContext.getBroadcaster().register(onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);
        openAndQuery();
    }


    @Override
    public void setOnCursorChangedListener(OnCursorChangedListener l) {
        onCursorChangedListener = l;
    }


    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
        if (sdirectory.hasKey(key)) {
            openAndQuery();

        } else if (sdirectory.containsKey(key) && !selection.equals(sdirectory.createSelectionString())) {
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

    public abstract void onCursorChanged(DbResultSet resultSet, Foc directory, String fid);


    private void openAndQuery() {
        String fileOnOldPosition = "";
        int oldPosition=0;

        appContext.getServices().getDirectoryService().openDir(sdirectory.getValueAsFile());
        selection = sdirectory.createSelectionString();
        resultSet = appContext.getServices().getDirectoryService().query(selection);

        if (resultSet != null) {
            resultSet.moveToPosition(oldPosition);

            onCursorChanged(resultSet, sdirectory.getValueAsFile(), fileOnOldPosition);
            onCursorChangedListener.onCursorChanged();
        }
    }

    @Override
    public void query() {
        String fileOnOldPosition = "";
        int oldPosition=0;

        if (resultSet != null) {
            oldPosition = resultSet.getPosition();
            fileOnOldPosition = getInfo().getFile().getPath();
            resultSet.close();
        }

        selection = sdirectory.createSelectionString();
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
