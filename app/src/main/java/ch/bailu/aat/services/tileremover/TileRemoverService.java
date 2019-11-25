package ch.bailu.aat.services.tileremover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public final class TileRemoverService extends VirtualService {


    final private StateMachine state;

    private boolean locked=false;

    public TileRemoverService(ServiceContext sc) {
        super(sc);

        AppBroadcaster.register(sc.getContext(), onStop, AppBroadcaster.TILE_REMOVER_STOPPED);
        AppBroadcaster.register(sc.getContext(), onRemove, AppBroadcaster.TILE_REMOVER_REMOVE);
        state = new StateMachine(sc);
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


    @Override
    public void appendStatusText(StringBuilder builder) {

    }



    @Override
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
