package ch.bailu.aat.services.background;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.util_java.foc.Foc;

public class DownloadHandle extends ProcessHandle {


    private final static String USER_AGENT_KEY = "User-Agent";
    private final static String USER_AGENT_VALUE = 
            AppTheme.APP_SHORT_NAME + "/" + AppTheme.APP_LONG_NAME + " (aat@bailu.ch)";

    private final static int IO_BUFFER_SIZE=8*1024;

    private final String url;
    private final Foc file;

    private boolean downloadLock=false;


    public DownloadHandle(String source, Foc target) {
        file = target;
        url = source;

    }


    @Override
    public long bgOnProcess(ServiceContext sc) {
        try {
            long r = bgDownload();
            AppBroadcaster.broadcast(sc.getContext(),
                    AppBroadcaster.FILE_CHANGED_ONDISK, file, url);
            return r;

        } catch (Exception e) {
            logError(e);
            file.rm();
            return 0;
        }

    }


    protected long bgDownload() throws IOException {
        return download(new URL(url), file);
    }


    protected void logError(Exception e) {
        AppLog.d(this, "ERROR: " + e.toString());
    }


    @Override
    public String toString() {
        return url;
    }
    

    @Override
    public boolean isLocked() {
        return downloadLock;
    }



    private long download(URL url, Foc file) throws IOException {
        int count;
        long total=0;
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        byte[] buffer = new byte[IO_BUFFER_SIZE];


        try {
            downloadLock=true;
            output = file.openW();

            connection = openConnection(url);
            input = openInput(connection);

            while (( count = input.read(buffer)) != -1) {
                total+=count;
                output.write(buffer, 0, count);
            }


        } finally {    
            Foc.close(output);
            downloadLock = false;

            Foc.close(input);
            if (connection != null) connection.disconnect();
        }

        return total;
    }







    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty(USER_AGENT_KEY, USER_AGENT_VALUE);
        return (HttpURLConnection) url.openConnection();

    }

    public static InputStream openInput(HttpURLConnection connection) throws IOException {
        return connection.getInputStream();
    }



    public Foc getFile() {
        return file;
    }



}
