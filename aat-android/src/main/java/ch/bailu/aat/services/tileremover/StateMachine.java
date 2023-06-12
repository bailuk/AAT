package ch.bailu.aat.services.tileremover;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidTrimIndex;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.foc.Foc;

public final class StateMachine implements State {

    private State state;

    public TilesList list = null;
    public final SourceSummaries summaries;

    public Foc baseDirectory;

    public final Context context;


    public StateMachine(ServiceContext sc) {
        summaries = new SourceSummaries(sc.getContext());
        context = sc.getContext();
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

    @Override
    public synchronized void removeAll() {
        state.removeAll();
    }


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

        } else if (s == StateRemoveAll.class) {
            set(new StateRemoveAll(this));
        }

    }

    public SelectedTileDirectoryInfo getInfo() {
        int index = new SolidTrimIndex(new Storage(context)).getValue();

        String name = summaries.get(index).getName();

        Foc subDirectory = baseDirectory;
        if (index > 0) subDirectory = baseDirectory.child(name);

        return new SelectedTileDirectoryInfo(baseDirectory, subDirectory, name, index);
    }



    private static final long LIMIT = 100;
    private long stamp;

    public void broadcastLimited(String msg) {
        long stamp = System.currentTimeMillis();

        if (stamp - this.stamp > LIMIT) {
            this.stamp = stamp;
            broadcast(msg);
        }
    }

    public void broadcast(String msg) {
        OldAppBroadcaster.broadcast(context, msg);
    }

}
