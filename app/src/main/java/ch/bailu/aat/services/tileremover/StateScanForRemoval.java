package ch.bailu.aat.services.tileremover;

import java.util.Iterator;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.SolidTrimDate;
import ch.bailu.aat.preferences.SolidTrimIndex;
import ch.bailu.aat.preferences.SolidTrimMode;
import ch.bailu.aat.preferences.SolidTrimSize;

public class StateScanForRemoval implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateScanned.class;


    private final int trimMode, trimDirectoryHash, trimIndex;
    private final long trimSize;
    private final long trimAge;



    public StateScanForRemoval(StateMachine s) {
        state = s;

        trimMode = new SolidTrimMode(s.context).getIndex();
        trimSize = new SolidTrimSize(s.context).getValue();
        trimAge = System.currentTimeMillis() - new SolidTrimDate(s.context).getValue();

        trimIndex = new SolidTrimIndex(s.context).getValue();

        if (trimIndex > 0) {
            trimDirectoryHash = state.summaries.hashCode(trimIndex);
        } else {
            trimDirectoryHash = 0;
        }




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
        nextState = StateScan.class;
    }

    @Override
    public void remove() {

    }

    @Override
    public void rescan() {
        nextState = StateScanForRemoval.class;
    }


    @Override
    public void run() {
        final Iterator<TileFile> iterator = state.list.iterator();

        int c=0;
        while (iterator.hasNext()) {
            TileFile file = iterator.next();

            if (c > 0) {
                c--;
            } else if (keepUp()){
                c=500;
                broadcast();
            } else {
                break;
            }

            if (passFilter(file)) {
                if (passDirectory(file)) addFile(file);

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
        return trimDirectoryHash == 0 || file.hashCode() == trimDirectoryHash;
    }


    private boolean passSize() {
        return state.summaries.getNewSize(trimIndex) > trimSize;
    }

    private boolean passAge(TileFile file) {
        return file.lastModified() < trimAge;
    }

    private boolean keepUp() {
        return (nextState == StateScanned.class);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_SCAN);
    }
}
