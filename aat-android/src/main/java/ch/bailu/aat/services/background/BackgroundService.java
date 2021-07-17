package ch.bailu.aat.services.background;

import java.net.URL;
import java.util.HashMap;

import ch.bailu.aat.preferences.map.SolidRendererThreads;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.util.WithStatusText;
import ch.bailu.foc.Foc;

public final class BackgroundService extends VirtualService implements WithStatusText {

    final static int FILE_LOADER_BASE_DIRECTORY_DEPTH = 4;

    private final Tasks tasks = new Tasks();

    private final HashMap<String, DownloaderThread> downloaders = new HashMap<>(5);
    private final HashMap<String, LoaderThread> loaders = new HashMap<>(5);


    private final HandleStack queue;


    private final WorkerThread[] workers =
            new WorkerThread[SolidRendererThreads.numberOfBackgroundThreats()];


    private final ServiceContext scontext;

    public BackgroundService(final ServiceContext sc) {
        scontext = sc;
        queue = new HandleStack(sc.getContext());
        for (int i=0; i< workers.length; i++)
            workers[i] = new WorkerThread("WT_" + i, sc, queue);


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

