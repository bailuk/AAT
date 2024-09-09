package ch.bailu.aat_lib.service.background;

import ch.bailu.aat_lib.app.AppContext;

public final class LoaderThread extends WorkerThread {
    private static final int LOADER_QUEUE_SIZE = 100;

    private final String directory;

    private int total_loads=0;
    private long total_bytes=0;


    public LoaderThread(AppContext sc, String d) {
        super("LT_" + d, sc, LOADER_QUEUE_SIZE);
        directory=d;
    }


    @Override
    public void bgOnHandleProcessed(BackgroundTask handle, long size) {
        total_loads++;
        total_bytes+=size;
    }

    public void appendStatusText(StringBuilder builder) {
        builder.append("<h2>");
        builder.append(directory);
        builder.append("</h2>");
        builder.append("<p>Loads: ");
        builder.append(total_loads);
        builder.append("<br>Total bytes: ");
        builder.append(total_bytes);
        builder.append(" bytes");

        if (total_loads >0) {
            builder.append("<br>Average bytes: ");
            builder.append(Math.round(total_bytes/(float)total_loads));
            builder.append(" bytes");
        }

        builder.append("</p>");
    }
}
