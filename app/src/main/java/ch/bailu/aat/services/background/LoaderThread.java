package ch.bailu.aat.services.background;

import android.content.Context;

public class LoaderThread extends ProcessThread {
    private final Context context;
    private final String directory;
    
    private int total_loads=0;
    private int total_bytes=0;
    
    
    private static final int SIZE=100;

    public LoaderThread(Context c, String d) {
        super(SIZE);
        directory=d;
        context = c;
    }



    @Override
    public void bgOnHaveHandle(ProcessHandle handle) {
        handle.bgLock();
        total_loads++;
        total_bytes+=handle.bgOnProcess();
        
        handle.bgUnlock();
        handle.broadcast(context);

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
            builder.append(Math.round(total_bytes/total_loads));
            builder.append(" bytes");
        }


        builder.append("</p>");        
    }
}

