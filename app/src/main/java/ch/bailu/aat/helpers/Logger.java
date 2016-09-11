package ch.bailu.aat.helpers;

import java.io.IOException;

public class Logger {
    private final String source;

    public Logger(Class<?> s) {
        source = s.getSimpleName();
    }

    public void debug(String string) {
        AppLog.d(source, string);
    }

    public void error(String string) {
        AppLog.e(source,  string);
    }

    public void error(String string, Throwable e) {
        AppLog.e(source, e);
    }

    public void warn(String string) {
        AppLog.d(source, string);
    }

    public void info(String string) {
        AppLog.i(source , string);
    }

    public void error(String string, String string2, IOException e) {
        String m = string + " " + string2;

        if (e.getMessage()!= null) m = m + " " + e.getMessage();
        AppLog.e(source, m);
    }

    public void trace(String string) {
        AppLog.d(source, string);
    }

    public void info(String string, Throwable e) {
        if (e.getMessage() != null) 
            AppLog.i(source, string + " " + e.getMessage());
        else {
            info(string);
        }
    }
}
