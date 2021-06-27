package ch.bailu.aat.services.background;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.aat.util.net.URX;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppConfig;
import ch.bailu.foc.Foc;

public class DownloadTask extends FileTask implements ContextWrapperInterface{


    private final static int TIMEOUT = 30 * 1000;
    private final static String USER_AGENT_KEY = "User-Agent";
    private final static String USER_AGENT_VALUE = AppConfig.getUserAgent();

    private final static int IO_BUFFER_SIZE=8*1024;

    private final URX urx;

    private final Context context;

    public DownloadTask(Context c, String source, Foc target) {
        this(c, new URX(source), target);
    }


    public DownloadTask(Context c, URX source, Foc target) {
        super(target);

        urx = source;
        context = c;
    }




    @Override
    public long bgOnProcess(ServiceContext sc) {
        long size = 0;

        try {
            size = bgDownload();
            AppBroadcaster.broadcast(sc.getContext(),
                    AppBroadcaster.FILE_CHANGED_ONDISK, getFile(), urx);

        } catch (Exception e) {
            logError(e);
            setException(e);
            getFile().rm();

        }
        return size;
    }



    protected long bgDownload() throws IOException {
        return download(urx.toURL(), getFile());
    }


    protected void logError(Exception e) {
        AppLog.w(this, getFile().getPathName());
        AppLog.w(this, e);
    }


    @NonNull
    @Override
    public String toString() {
        return urx.toString();
    }



    private long download(URL url, Foc file) throws IOException {
        int count;
        long total=0;
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection;
        byte[] buffer = new byte[IO_BUFFER_SIZE];


        try {
            output = file.openW();

            connection = openConnection(url);
            input = openInput(connection);


            while (( count = input.read(buffer)) != -1) {

                total+=count;
                output.write(buffer, 0, count);
            }


        } finally {
            Foc.close(output);
            Foc.close(input);
        }

        return total;
    }



    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setRequestProperty(USER_AGENT_KEY, USER_AGENT_VALUE);
        return connection;

    }


    public static InputStream openInput(HttpURLConnection connection) throws IOException {
        return connection.getInputStream();
    }


    public URX getSource() {
        return urx;
    }

    @Override
    public Context getContext() {
        return context;
    }


}
