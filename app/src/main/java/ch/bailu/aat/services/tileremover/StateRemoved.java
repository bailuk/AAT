package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.util.ui.AppLog;

public class StateRemoved extends StateUnscanned {
    public StateRemoved(StateMachine s) {
        super(s);
        AppLog.d(this, "removed()");
    }
}


/*    implements
} State {
    private final StateMachine state;



    public StateRemoved(StateMachine s) {
        state = s;
        state.list=null;

        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_STOPPED);
    }



    @Override
    public void scan() {
        state.set(new StateScan(state));
    }

    @Override
    public void stop() {}


    @Override
    public void reset() {
        state.set(new StateUnscanned(state));
    }

    @Override
    public void remove() {}

    @Override
    public void removeLayers() {}

    @Override
    public void rescan() {}
}
*/