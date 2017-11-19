package ch.bailu.aat.services.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import ch.bailu.aat.preferences.SolidRendererThreads;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public class BackgroundService extends VirtualService {

    private final HashMap<String, DownloaderThread> downloaders = new HashMap<>(5);
    private final HashMap<String, LoaderThread> loaders = new HashMap<>(5);


    private final WorkerThread workers[] =
            new WorkerThread[SolidRendererThreads.numberOfBackgroundThreats()];


    private final BroadcastReceiver onFileChangedOnDisk = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Foc file = FocAndroid.factory(context, AppIntent.getFile(intent));
            AppLog.i(context, file.getPathName());
        }
    };

    public BackgroundService(final ServiceContext sc) {
        super(sc);

        AppBroadcaster.register(getContext(), onFileChangedOnDisk, AppBroadcaster.FILE_CHANGED_ONDISK);

        HandleQueue queue = new HandleQueue();
        for (int i=0; i< workers.length; i++)
            workers[i] =new WorkerThread(sc, queue);


    }

    public void process(ProcessHandle handle) {
        workers[0].process(handle);
    }


    public void download(ProcessHandle handle) {
        URL url;
        try {
            url = new URL(handle.toString());
        } catch (MalformedURLException e) {
            url = null;
        }

        if (url != null) {
            String host = url.getHost();
            DownloaderThread downloader = downloaders.get(host);

            if (downloader == null) {
                downloader = new DownloaderThread(getSContext(), host);
                downloaders.put(host, downloader);
            }
            downloader.process(handle);
        }
    }



    public void load(ProcessHandle handle) {
        final String base = getBaseDirectory(handle.toString());

        LoaderThread loader = loaders.get(base);

        if (loader == null) {
            loader = new LoaderThread(getSContext(), base);
            loaders.put(base, loader);
        }
        loader.process(handle);
    }


    @Override
    public void close() {
        getContext().unregisterReceiver(onFileChangedOnDisk);

        for(ProcessThread p: loaders.values())
            p.close();
        loaders.clear();

        for (DownloaderThread downloader: downloaders.values())
            downloader.close();
        downloaders.clear();

        for (WorkerThread w: workers)
            w.close();
    }


    private String getBaseDirectory(String id) {
        File p1 = new File (id);
        File r = p1;

        int c=0;
        final int t=3;

        while (p1!=null) {
            p1=p1.getParentFile();

            if (c<t) {
                c++;

            } else {
                r=r.getParentFile();

            }

        }

        return r.getAbsolutePath();
    }

    @Override
    public void appendStatusText(StringBuilder builder) {

        for (LoaderThread p: loaders.values())
            p.appendStatusText(builder);

        for (DownloaderThread p: downloaders.values())
            p.appendStatusText(builder);
    }
}

