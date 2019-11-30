package ch.bailu.aat.services.background;


import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.net.URX;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.util.Objects;

public final class Downloads {

    private final ArrayList<DownloadTask> downloads = new ArrayList<>(10);

    public Downloads() {}


    public synchronized void add(DownloadTask t) {
        if (!contains(t)) {
            downloads.add(t);
            changed(t.getContext(), t);
        }
    }


    public boolean contains(DownloadTask t) {
        return contains(t.getFile());
    }

    public synchronized DownloadTask get(Foc file) {
        for (DownloadTask t : downloads) {
            if (Objects.equals(t.getFile(), file)) {
                return t;
            }
        }
        return null;

    }

    public synchronized boolean contains(Foc file) {
        return get(file) != null;
    }


    public synchronized void remove(DownloadTask t) {
        if (downloads.remove(t)) {
            changed(t.getContext(), t);
        }
    }

    private synchronized void changed(Context c, DownloadTask t) {
        AppBroadcaster.broadcast(c,
                AppBroadcaster.ON_DOWNLOADS_CHANGED, t.getFile(), t.getSource());
    }

    public synchronized Foc getFile() {
        if (downloads.isEmpty()==false) return downloads.get(0).getFile();

        return null;
    }

    public synchronized URX getSource() {
        if (downloads.isEmpty()==false) return downloads.get(0).getSource();

        return null;
    }

}
