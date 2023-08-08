package ch.bailu.aat_lib.service.background;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.util.net.URX;
import ch.bailu.foc.Foc;

public class DownloadTask extends FileTask {

    private final URX urx;
    private final DownloadConfig downloadConnection;

    public DownloadTask(String source, Foc target, DownloadConfig downloadConnection) {
        this(new URX(source), target, downloadConnection);
    }

    public DownloadTask(URX source, Foc target, DownloadConfig downloadConnection) {
        super(target);
        this.downloadConnection = downloadConnection;
        urx = source;
    }

    @Override
    public long bgOnProcess(AppContext sc) {
        long size = 0;

        try {
            size = bgDownload();
            sc.getBroadcaster().broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK, getFile().toString(), urx.toString());

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

    @Nonnull
    @Override
    public String toString() {
        return urx.toString();
    }

    private long download(URL url, Foc file) {
        int count;
        long total=0;
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection;
        byte[] buffer = downloadConnection.createBuffer();

        try {
            output = file.openW();

            connection = downloadConnection.openConnection(url);
            input = downloadConnection.openInput(connection);

            while ((count = input.read(buffer)) != -1) {
                total += count;
                output.write(buffer, 0, count);
            }

        } catch (Exception e) {
            AppLog.e("Download " + url + " -> " + file + ": " + e.getMessage());

        } finally {
            Foc.close(output);
            Foc.close(input);
        }

        return total;
    }

    public URX getSource() {
        return urx;
    }
 }
