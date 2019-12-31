package ch.bailu.aat.util;

import android.content.Context;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.background.DownloadTask;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public abstract class DownloadApi extends OsmApiHelper {

    private BackgroundTask task = BackgroundTask.NULL;

    private static class ApiQueryTask extends DownloadTask {
        private final String queryString;
        private final Foc queryFile;

        public ApiQueryTask(Context c, String source, Foc target, String qs, Foc qf) {
            super(c, source, target);
            queryString = qs;
            queryFile   = qf;
        }


        @Override
        public long bgOnProcess(ServiceContext sc) {
            try {
                long size = bgDownload();
                TextBackup.write(queryFile, queryString);

                AppBroadcaster.broadcast(sc.getContext(),
                        AppBroadcaster.FILE_CHANGED_ONDISK, getFile(), getSource());

                return size;
            } catch (Exception e) {
                logError(e);
                return 1;
            }
        }

        @Override
        protected void logError(Exception e) {
            AppLog.e(getContext(), e);
        }
    }





    @Override
    public void startTask(ServiceContext scontext) {
        new InsideContext(scontext) {
            @Override
            public void run() {

                try {
                    BackgroundService background = scontext.getBackgroundService();

                    final String query = getQueryString();
                    task = new ApiQueryTask(
                            scontext.getContext(),
                            getUrl(query),
                            getResultFile(),
                            query,
                            getQueryFile());
                    background.process(task);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    protected abstract String getQueryString();


    @Override
    public Exception getException() {
        return task.getException();
    }

}
