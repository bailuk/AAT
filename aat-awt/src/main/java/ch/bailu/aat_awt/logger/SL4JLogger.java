package ch.bailu.aat_awt.logger;

public class SL4JLogger implements  ch.bailu.aat_lib.logger.Logger {

    @Override
    public void w(String tag, String msg) {
        SL4JCache.SELF.get(tag).warn(msg);
    }

    @Override
    public void i(String tag, String msg) {
        SL4JCache.SELF.get(tag).info(msg);
    }

    @Override
    public void d(String tag, String msg) {
        SL4JCache.SELF.get(tag).debug(msg);
    }

    @Override
    public void e(String tag, String msg) {
        SL4JCache.SELF.get(tag).error(msg);
    }
}
