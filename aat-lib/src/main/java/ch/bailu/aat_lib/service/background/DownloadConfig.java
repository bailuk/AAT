package ch.bailu.aat_lib.service.background;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.bailu.aat_lib.app.AppConfig;

public class DownloadConfig {

    private final static int TIMEOUT = 30 * 1000;
    private final static String USER_AGENT_KEY = "User-Agent";
    private final String userAgentValue;

    private final static int IO_BUFFER_SIZE=8*1024;

    public DownloadConfig(AppConfig config) {
        userAgentValue = config.getUserAgent();
    }

    public byte[] createBuffer() {
        return new byte[IO_BUFFER_SIZE];
    }

    public HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setRequestProperty(USER_AGENT_KEY, userAgentValue);
        return connection;
    }




    public InputStream openInput(HttpURLConnection connection) throws IOException {
        return connection.getInputStream();
    }

}
