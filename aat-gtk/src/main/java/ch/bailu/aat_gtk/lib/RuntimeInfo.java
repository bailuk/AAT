package ch.bailu.aat_gtk.lib;

import ch.bailu.gtk.lib.util.SizeLog;

public class RuntimeInfo implements Runnable {

    private static final int MB = 1024*1024;

    @Override
    public void run() {
        var max = new SizeLog("Runtime:Max");
        var total = new SizeLog("Runtime:Total");
        var free = new SizeLog("Runtime:Free");
        var used = new SizeLog("Runtime:Used");
        var processors = new SizeLog("Runtime:Processors");

        var runtime = Runtime.getRuntime();

        var on = true;
        while (on) {
            try {
                Thread.sleep(5000);
                max.log(runtime.maxMemory() / MB);
                total.log(runtime.totalMemory() / MB);
                free.log(runtime.freeMemory() / MB);
                used.log((runtime.totalMemory() - runtime.freeMemory())/MB);
                processors.log(runtime.availableProcessors() / MB);

            } catch (Exception e) {
                System.err.println(e.getMessage());
                on = false;
            }
        }
    }
}
