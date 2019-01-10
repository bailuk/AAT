package ch.bailu.aat.services.tileremover;

import java.util.Iterator;

import ch.bailu.aat.preferences.map.SolidTrimDate;
import ch.bailu.aat.preferences.map.SolidTrimIndex;
import ch.bailu.aat.preferences.map.SolidTrimMode;
import ch.bailu.aat.preferences.map.SolidTrimSize;
import ch.bailu.aat.util.AppBroadcaster;

public class StateScanForRemoval implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateScanned.class;


    private final int trimMode, trimSummaryIndex;
    private final long trimSize;
    private final long trimAge;



    public StateScanForRemoval(StateMachine s) {
        state = s;

        trimMode = new SolidTrimMode(s.context).getIndex();
        trimSize = new SolidTrimSize(s.context).getValue();
        trimAge = System.currentTimeMillis() - new SolidTrimDate(s.context).getValue();



        trimSummaryIndex = new SolidTrimIndex(s.context).getValue();


        state.summaries.resetToRemove();
        state.list.resetToRemove();

        new Thread(this).start();


    }


    @Override
    public void scan() {
        nextState = StateScanned.class;
    }

    @Override
    public void stop() {
        nextState = StateScannedPartial.class;

    }

    @Override
    public void reset() {
        nextState = StateUnscanned.class;
    }


    @Override
    public void remove() {}

    @Override
    public void removeAll() { nextState = StateRemoveAll.class; }


    @Override
    public void rescan() {
        nextState = StateScanForRemoval.class;
    }


    @Override
    public void run() {
        final Iterator<TileFile> iterator = state.list.iterator();

        while (iterator.hasNext() && keepUp()) {
            TileFile file = iterator.next();

            if (passFilter(file)) {
                if (passDirectory(file)) addFile(file);
                state.broadcastLimited(AppBroadcaster.TILE_REMOVER_SCAN);
            } else {
                break;
            }


        }

        state.setFromClass(nextState);
    }

    private void addFile(TileFile file) {
        state.summaries.addFileToRemove(file);
        state.list.addToRemove(file);
    }

    private boolean passFilter(TileFile file) {
        if        (trimMode == SolidTrimMode.MODE_TO_SIZE) {
            return passSize();

        } else if (trimMode == SolidTrimMode.MODE_TO_AGE) {
            return passAge(file);

        } else if (trimMode == SolidTrimMode.MODE_TO_SIZE_AND_AGE) {
            return passSize() || passAge(file);

        } else if (trimMode == SolidTrimMode.MODE_TO_SIZE_OR_AGE) {
            return passSize() && passAge(file);
        }
        return false;
    }


    private boolean passDirectory(TileFile file) {
        return trimSummaryIndex == 0 || file.getSource() == trimSummaryIndex;
    }


    private boolean passSize() {
        return state.summaries.get(trimSummaryIndex).sizeNew > trimSize;
    }

    private boolean passAge(TileFile file) {
        return file.lastModified() < trimAge;
    }

    private boolean keepUp() {
        return (nextState == StateScanned.class);
    }

}
