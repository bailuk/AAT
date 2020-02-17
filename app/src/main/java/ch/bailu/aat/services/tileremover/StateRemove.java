package ch.bailu.aat.services.tileremover;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public final class StateRemove implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateRemoved.class;


    public StateRemove(StateMachine s) {
        state = s;
        new Thread(this).start();
    }

    @Override
    public void scan() {}

    @Override
    public void stop() {
        nextState = StateScanned.class;
    }


    @Override
    public void reset() {
        nextState = StateUnscanned.class;
    }

    @Override
    public void remove() {}

    @Override
    public void removeAll() {}

    @Override
    public void rescan() {}


    private class TaskDelete implements Callable<TileFile> {

        public final TileFile tileFile;

        private TaskDelete(TileFile tileFile) {
            this.tileFile = tileFile;


        }

        @Override
        public TileFile call() {
            final Foc f = state.summaries.toFile(state.baseDirectory, tileFile);

            if (f.rm()) {
                return tileFile;
            }

            return null;
        }
    }


    @Override
    public void run() {
        final Iterator<TileFile> iterator = state.list.iteratorToRemove();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<TileFile> completion = new ExecutorCompletionService<>(executor);


        int tasks=0;

        while (iterator.hasNext() && keepUp()) {
            final TileFile t = iterator.next();
            completion.submit(new TaskDelete(t));
            tasks++;
        }

        while(tasks > 0 && keepUp()) {
            tasks--;
            try {
                Future<TileFile> future = completion.take();
                TileFile t = future.get();
                if (t != null) {
                     state.summaries.addFileRemoved(t);
                }
                state.broadcastLimited( AppBroadcaster.TILE_REMOVER_REMOVE);
            } catch (InterruptedException e) {
                AppLog.w(this,e);
                break;
            } catch (ExecutionException e) {
                AppLog.w(this, e);
            }
        }

        executor.shutdownNow();

        state.list.resetToRemove();

        if (keepUp()) {
            state.broadcast(AppBroadcaster.TILE_REMOVER_REMOVE);
            state.baseDirectory.rmdirs();
        }

        state.setFromClass(nextState);
    }


    private boolean keepUp() {
        return (nextState == StateRemoved.class);
    }

}
