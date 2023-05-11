package ch.bailu.aat_lib.logger;

@FunctionalInterface
public interface Logger {
    void log(String tag, String msg);
}
