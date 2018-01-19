package ch.bailu.aat.services.background;


import java.util.ArrayList;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.util.Objects;

public class Downloads {

    private static final ArrayList<DownloadTask> downloads = new ArrayList<>(10);

    private Downloads() {}


    public synchronized static void add(DownloadTask t) {
        if (!contains(t)) {
            downloads.add(t);
            changed(t);
        }
    }


    public synchronized static boolean contains(DownloadTask t) {
        return downloads.contains(t);
    }

    public synchronized static DownloadTask get(Foc file) {
        for (DownloadTask d : downloads) {
            if (Objects.equals(d.getFile(), file)) {
                return d;
            }
        }
        return null;

    }

    public synchronized static boolean contains(Foc file) {
        return get(file) != null;
    }


    public synchronized static void remove(DownloadTask t) {
        if (downloads.remove(t))
            changed(t);
    }

    private synchronized static void changed(DownloadTask t) {
        AppBroadcaster.broadcast(t.getContext(),
                AppBroadcaster.ON_DOWNLOADS_CHANGED, t.getFile(), t.getSource());
    }
}
