package ch.bailu.aat_lib.logger;

public class JavaLogger implements Logger{
    @Override
    public void w(String tag, String msg) {
        java.util.logging.Logger.getLogger(tag).warning(msg);
    }

    @Override
    public void i(String tag, String msg) {
        java.util.logging.Logger.getLogger(tag).info(msg);
    }

    @Override
    public void d(String tag, String msg) {
        java.util.logging.Logger.getLogger(tag).fine(msg);
    }

    @Override
    public void e(String tag, String msg) {
        java.util.logging.Logger.getLogger(tag).severe(msg);
    }
}
