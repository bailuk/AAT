package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class TileRemoverService extends VirtualService {


    private StateMachine state;

    public TileRemoverService(ServiceContext sc) {
        super(sc);

        state = new StateMachine(getContext());
    }

    @Override
    public void appendStatusText(StringBuilder builder) {

    }

    @Override
    public void close() {

    }

    public State getState() {
        return state;
    }


    public MapSummaryInterface[] getSummaries() {
        return state.summaries.getMapSummaries();
    }
}
