package ch.bailu.aat.views;

import android.content.Context;
import android.widget.ProgressBar;
import ch.bailu.aat.helpers.AppBroadcaster;

public class DbSynchronizerBusyIndicator extends BusyIndicator {

    private final OnSyncAction 
    onSyncStart = new OnSyncAction(ProgressBar.VISIBLE),
    onSyncChanged  = new OnSyncAction(ProgressBar.VISIBLE),
    onSyncDone = new OnSyncAction(ProgressBar.INVISIBLE);


    public DbSynchronizerBusyIndicator(Context c) {
        super(c);

        setVisibility(ProgressBar.INVISIBLE);

        AppBroadcaster.register(getContext(), onSyncStart,   AppBroadcaster.DBSYNC_START);
        AppBroadcaster.register(getContext(), onSyncDone,    AppBroadcaster.DBSYNC_DONE);
        AppBroadcaster.register(getContext(), onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);

    }


    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onSyncChanged);
        getContext().unregisterReceiver(onSyncDone);
        getContext().unregisterReceiver(onSyncStart);
        super.onDetachedFromWindow();
    }
}
