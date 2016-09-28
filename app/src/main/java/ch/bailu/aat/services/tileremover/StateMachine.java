package ch.bailu.aat.services.tileremover;

import android.content.Context;

import java.io.File;

public class StateMachine implements State {

    private State state;

    public final TilesList list = new TilesList();
    public final TilesSummaries summaries = new TilesSummaries();

    public File tileDirectory;

    public final Context context;


    public StateMachine(Context c) {
        context = c;
        set(new StateUnscanned(this));
    }

    public synchronized void set(State s) {
        state = s;
    }


    @Override
    public synchronized void scan() {
        state.scan();
    }

    @Override
    public synchronized void stop() {
        state.stop();
    }

    @Override
    public synchronized void reset() {
        state.reset();
    }

    @Override
    public synchronized void remove() {
        state.remove();
    }

    @Override
    public synchronized void rescan() { state.rescan(); }


    public synchronized void setFromClass(Class s) {
        if (s == StateRemoved.class) {
            set(new StateRemoved(this));

        } else  if (s == StateScanned.class) {
            set(new StateScanned(this));

        } else if (s == StateUnscanned.class){
            set(new StateUnscanned(this));

        } else if (s == StateScannedPartial.class) {
            set(new StateScannedPartial(this));

        } else if (s == StateScanForRemoval.class) {
            set(new StateScanForRemoval(this));

        } else if (s == StateScan.class) {
            set(new StateScan(this));
        }

    }
}
