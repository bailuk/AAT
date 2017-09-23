package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public class LoaderThread extends ProcessThread {
    private static final int LOADER_QUEUE_SIZE = 100;

    private final ServiceContext scontext;
    private final String directory;
    
    private int total_loads=0;
    private long total_bytes=0;
    
    
    public LoaderThread(ServiceContext sc, String d) {
        super(LOADER_QUEUE_SIZE);
        directory=d;
        scontext = sc;
    }



    @Override
    public void bgOnHaveHandle(ProcessHandle handle) {
        handle.bgLock();
        total_loads++;
        total_bytes+=handle.bgOnProcess(scontext);
        
        handle.bgUnlock();


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

