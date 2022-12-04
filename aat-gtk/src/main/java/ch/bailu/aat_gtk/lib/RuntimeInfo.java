package ch.bailu.aat_gtk.lib;

import ch.bailu.gtk.lib.util.SizeLog;

public class RuntimeInfo implements Runnable {

    private static final int MB = 1024*1024;
    private static final long TIMEOUT = 5000;
    private static boolean on = false;

    @Override
    public void run() {
        var max = new SizeLog(getIdentifier("Max"));
        var total = new SizeLog(getIdentifier("Total"));
        var free = new SizeLog(getIdentifier("Free"));
        var used = new SizeLog(getIdentifier("Used"));
        var processors = new SizeLog(getIdentifier("Processors"));

        var runtime = Runtime.getRuntime();

        while (on) {
            try {
                Thread.sleep(TIMEOUT);
                max.log(runtime.maxMemory() / MB);
                total.log(runtime.totalMemory() / MB);
                free.log(runtime.freeMemory() / MB);
                used.log((runtime.totalMemory() - runtime.freeMemory()) / MB);
                processors.log(runtime.availableProcessors() / MB);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                on = false;
            }
        }
    }

    private static String getIdentifier(String name) {
        return Runtime.class.getSimpleName() + ":" + name;
    }

    public static synchronized void startLogging() {
        if (!on) {
            on = true;
            new Thread(new RuntimeInfo()).start();
        }
    }
}
