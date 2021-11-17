package ch.bailu.aat.util;

import android.content.Context;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.background.DownloadTask;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.foc.Foc;

public abstract class DownloadApi extends OsmApiConfiguration {

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

                OldAppBroadcaster.broadcast(sc.getContext(),
                        AppBroadcaster.FILE_CHANGED_ONDISK, getFile(), getSource());

                return size;
            } catch (Exception e) {
                setException(e);
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
                    BackgroundServiceInterface background = scontext.getBackgroundService();



                    final String query = getQueryString();
                    final String url = getUrl(query);

                    task = new ApiQueryTask(
                            scontext.getContext(),
                            url,
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
