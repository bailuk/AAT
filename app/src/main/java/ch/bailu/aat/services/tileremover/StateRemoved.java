package ch.bailu.aat.services.tileremover;

public class StateRemoved extends StateUnscanned {
    public StateRemoved(StateMachine s) {
        super(s);
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
    public void removeAll() {}

    @Override
    public void rescan() {}
}
*/