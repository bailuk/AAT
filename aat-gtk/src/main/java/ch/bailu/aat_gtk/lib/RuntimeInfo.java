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

        try {
            Thread.sleep(TIMEOUT);
            processors.log(runtime.availableProcessors());
            free.log(runtime.freeMemory() / MB);

            while (on) {
                max.log(runtime.maxMemory() / MB);
                total.log(runtime.totalMemory() / MB);
                used.log((runtime.totalMemory() - runtime.freeMemory()) / MB);
                Thread.sleep(TIMEOUT);
            }

        } catch (InterruptedException e) {
            on = false;
            System.err.println(e.getMessage());
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

    public static synchronized void stopLogging() {
        on = false;
    }
}
