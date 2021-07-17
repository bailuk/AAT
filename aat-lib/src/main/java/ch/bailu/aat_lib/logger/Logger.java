package ch.bailu.aat_lib.logger;

public interface Logger {
    void w(String tag, String msg);
    void i(String tag, String msg);
    void d(String tag, String msg);
    void e(String tag, String msg);
}
