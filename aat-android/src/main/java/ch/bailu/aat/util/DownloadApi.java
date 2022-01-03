package ch.bailu.aat.util;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.background.DownloadTask;
import ch.bailu.foc.Foc;

public abstract class DownloadApi extends OsmApiConfiguration {

    private BackgroundTask task = BackgroundTask.NULL;

    private static class ApiQueryTask extends DownloadTask {
        private final String queryString;
        private final Foc queryFile;

        public ApiQueryTask(AppContext c, String source, Foc target, String qs, Foc qf) {
            super(source, target, c.getDownloadConfig());
            queryString = qs;
            queryFile   = qf;
        }


        @Override
        public long bgOnProcess(AppContext sc) {
            try {
                long size = bgDownload();
                TextBackup.write(queryFile, queryString);

                sc.getBroadcaster().broadcast(
                        AppBroadcaster.FILE_CHANGED_ONDISK, getFile().toString(), getSource().toString());

                return size;
            } catch (Exception e) {
                setException(e);
                return 1;
            }
        }

        @Override
        protected void logError(Exception e) {
            AppLog.e(e);
        }
    }





    @Override
    public void startTask(AppContext appContext) {
        new InsideContext(appContext.getServices()) {
            @Override
            public void run() {

                try {
                    BackgroundServiceInterface background = appContext.getServices().getBackgroundService();



                    final String query = getQueryString();
                    final String url = getUrl(query);

                    task = new ApiQueryTask(
                            appContext,
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
