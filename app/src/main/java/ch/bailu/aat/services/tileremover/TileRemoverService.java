package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class TileRemoverService extends VirtualService {


    final private StateMachine state;

    public TileRemoverService(ServiceContext sc) {
        super(sc);

        state = new StateMachine(sc);
    }


    @Override
    public void appendStatusText(StringBuilder builder) {

    }



    @Override
    public void close() {
        state.reset();
    }

    public State getState() {
        return state;
    }


    public MapSummaryInterface[] getSummaries() {
        return state.summaries.getMapSummaries();
    }
}
