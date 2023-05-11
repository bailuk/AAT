package ch.bailu.aat_lib.logger;

import java.io.PrintStream;

public class PrintLnLogger implements Logger {

    private final String prefix;
    private final PrintStream target;

    public PrintLnLogger(String prefix) {
        this(prefix, System.out);
    }


    public PrintLnLogger(String prefix, PrintStream target) {
        this.prefix = prefix;
        this.target = target;
    }

    @Override
    public void log(String tag, String msg) {
        target.println("[" + prefix + "] " + tag + ": " + msg);
    }
}
