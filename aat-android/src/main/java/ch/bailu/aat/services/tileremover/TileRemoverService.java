package ch.bailu.aat.services.tileremover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.service.VirtualService;

public final class TileRemoverService extends VirtualService {


    final private StateMachine state;

    private boolean locked=false;

    private final ServiceContext scontext;
    public TileRemoverService(ServiceContext sc) {
        scontext = sc;

        OldAppBroadcaster.register(sc.getContext(), onStop, AppBroadcaster.TILE_REMOVER_STOPPED);
        OldAppBroadcaster.register(sc.getContext(), onRemove, AppBroadcaster.TILE_REMOVER_REMOVE);
        state = new StateMachine(sc);
    }

    public ServiceContext getSContext() {
        return scontext;
    }

    public Context getContext() {
        return getSContext().getContext();
    }

    private final BroadcastReceiver onRemove = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            lock();
        }
    };


    private final BroadcastReceiver onStop = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            free();
        }
    };


    private void lock() {
        if (!locked) {
            locked = true;
            getSContext().lock(TileRemoverService.class.getSimpleName());
        }
    }

    private void free() {
        if (locked) {
            locked = false;
            getSContext().free(TileRemoverService.class.getSimpleName());
        }
    }

    public void close() {
        getContext().unregisterReceiver(onRemove);
        getContext().unregisterReceiver(onStop);
        state.reset();
    }

    public State getState() {
        return state;
    }


    public SelectedTileDirectoryInfo getInfo() {
       return state.getInfo();
    }



    public SourceSummaries getSummaries() {
        return state.summaries;
    }
}
