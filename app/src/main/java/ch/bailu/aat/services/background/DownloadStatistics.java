package ch.bailu.aat.services.background;


public class DownloadStatistics {
    private final static int MAX_RATE=5;
    private int failureRate=0;

    private long failure=0;
    private long success=0;
    private long bytes=0;

    private long nextDownload=System.currentTimeMillis() - 100;




    public synchronized void increment(long size) {
        bytes += size;
    }

    public synchronized  void failure() {
        failure++;
        failureRate = Math.min(MAX_RATE, failureRate+1);
        setNextDownloadTime();
    }

    public synchronized void success(long size) {
        increment(size);
        success();
    }

    public synchronized  void success() {
        failureRate = Math.max(0, failureRate-2);
        success++;
        setNextDownloadTime();
    }

    private  void setNextDownloadTime() {
        nextDownload = System.currentTimeMillis() + (failureRate*failureRate*1000) - 100;
    }

    public  synchronized boolean isReady() {
        return System.currentTimeMillis() > nextDownload;
    }

    public  synchronized void appendStatusText(StringBuilder builder, String server) {

        long time = getBlockIntervall();

        builder.append("<h2>");
        builder.append(server);
        builder.append("</h2>");
        builder.append("<p>Successfull downloads: ");
        builder.append(success);
        builder.append("<br>Total: ");
        builder.append(bytes);
        builder.append(" bytes");

        if (success > 0) {
            builder.append("<br>Average file pixelCount: ");
            builder.append(Math.round(bytes / (float)success));
            builder.append(" bytes");
        }

        builder.append("<br>Failed downloads: ");
        builder.append(failure);

        builder.append("<br>Downloads blocked for ");
        builder.append(time);
        builder.append(" ms</p>");

    }

    public  synchronized long getBlockIntervall() {
        return Math.max(0, nextDownload - System.currentTimeMillis());

    }
}
