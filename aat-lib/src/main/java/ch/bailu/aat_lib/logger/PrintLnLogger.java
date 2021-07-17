package ch.bailu.aat_lib.logger;

public class PrintLnLogger implements Logger {
    @Override
    public void w(String tag, String msg) {
        p("WARN", tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        p("INFO", tag,msg);
    }

    @Override
    public void d(String tag, String msg) {
        p("DEBUG", tag,msg);
    }

    @Override
    public void e(String tag, String msg) {
        p("ERROR", tag, msg);
    }

    private void p(String type, String tag, String msg) {
        System.out.println("[" + type + "] " + tag + ": " + msg);
    }
}
