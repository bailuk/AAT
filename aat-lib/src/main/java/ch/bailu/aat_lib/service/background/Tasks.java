package ch.bailu.aat_lib.service.background;


import java.util.ArrayList;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.util.Objects;
import ch.bailu.foc.Foc;

public final class Tasks {

    private final ArrayList<FileTask> downloads = new ArrayList<>(10);

    private final Broadcaster broadcaster;

    public Tasks(Broadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    public synchronized void add(FileTask t) {
        if (!contains(t)) {
            downloads.add(t);
            changed(t);
        }
    }


    public boolean contains(FileTask t) {
        return contains(t.getFile());
    }

    public synchronized FileTask get(Foc file) {
        for (FileTask t : downloads) {
            if (Objects.equals(t.getFile(), file)) {
                return t;
            }
        }
        return null;

    }

    public synchronized boolean contains(Foc file) {
        return get(file) != null;
    }


    public synchronized void remove(FileTask t) {
        if (downloads.remove(t)) {
            changed(t);
        }
    }

    private synchronized void changed(FileTask t) {

        if (t instanceof DownloadTask) {
            broadcaster.broadcast(
                    AppBroadcaster.FILE_BACKGROND_TASK_CHANGED,
                    t.getFile(),
                    ((DownloadTask) t).getSource().toString());

        } else {
            broadcaster.broadcast(
                    AppBroadcaster.FILE_BACKGROND_TASK_CHANGED,
                    t.getFile());
        }
    }
}
