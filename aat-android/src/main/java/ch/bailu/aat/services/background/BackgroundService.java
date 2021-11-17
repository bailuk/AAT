package ch.bailu.aat.services.background;

import java.net.URL;
import java.util.HashMap;

import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.background.DownloadTask;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.aat_lib.service.background.Tasks;
import ch.bailu.aat_lib.util.WithStatusText;
import ch.bailu.foc.Foc;

public final class BackgroundService extends VirtualService implements BackgroundServiceInterface, WithStatusText {

    final static int FILE_LOADER_BASE_DIRECTORY_DEPTH = 4;

    private final Tasks tasks;

    private final HashMap<String, DownloaderThread> downloaders = new HashMap<>(5);
    private final HashMap<String, LoaderThread> loaders = new HashMap<>(5);


    private final HandleStack queue;
    private final WorkerThread[] workers;
    private final ServicesInterface scontext;


    public BackgroundService(final ServicesInterface sc, Broadcaster broadcaster, int threads) {
        scontext = sc;
        tasks = new Tasks(broadcaster);
        queue = new HandleStack();
        workers = new WorkerThread[threads];
        for (int i=0; i< workers.length; i++) {
            workers[i] = new WorkerThread("WT_" + i, sc, queue);
        }
    }


    public void process(BackgroundTask handle) {
        if (handle instanceof DownloadTask) {
            download((DownloadTask) handle);

        } else if (handle instanceof FileTask) {
            load((FileTask) handle);

        } else {
            workers[0].process(handle);

        }
    }




    private void download(DownloadTask handle) {
        URL url = handle.getSource().getURL();

        if (url != null) {
            String host = url.getHost();
            DownloaderThread downloader = downloaders.get(host);

            if (downloader == null) {
                downloader = new DownloaderThread(scontext, host);
                downloaders.put(host, downloader);
            }

            handle.register(tasks);
            downloader.process(handle);
        }
    }




    private void load(FileTask handle) {
        final String base = getBaseDirectory(handle.getFile());

        LoaderThread loader = loaders.get(base);

        if (loader == null) {
            loader = new LoaderThread(scontext, base);
            loaders.put(base, loader);
        }

        handle.register(tasks);
        loader.process(handle);
    }


    public void close() {
        for(ProcessThread p: loaders.values())
            p.close();
        loaders.clear();

        for (DownloaderThread downloader: downloaders.values())
            downloader.close();
        downloaders.clear();

        for (WorkerThread w: workers)
            w.close();

        queue.close(workers.length);
    }


    private String getBaseDirectory(Foc r) {
        Foc p = r.parent();
        int depth = 0;

        while (p != null && depth < FILE_LOADER_BASE_DIRECTORY_DEPTH) {
            r = p;
            p = p.parent();

            depth++;
        }

        return r.getPathName();
    }



    @Override
    public void appendStatusText(StringBuilder builder) {

        for (LoaderThread p: loaders.values())
            p.appendStatusText(builder);

        for (DownloaderThread p: downloaders.values())
            p.appendStatusText(builder);
    }


    public FileTask findTask(Foc file) {
        return tasks.get(file);
    }
}

