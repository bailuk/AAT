package ch.bailu.aat.util.ui;

import java.io.IOException;

import ch.bailu.aat.util.ui.AppLog;

public class Logger {
    /** Osmdroid compability **/


    private final String source;

    public Logger(Class<?> s) {
        source = s.getSimpleName();
    }


    public void info(String string) {
        AppLog.d(source , string);
    }

    public void error(String string, String string2, IOException e) {
        String m = string + " " + string2;

        if (e.getMessage()!= null) m = m + " " + e.getMessage();
        AppLog.d(source, m);
    }


    public void info(String string, Throwable e) {
        if (e.getMessage() != null)
            AppLog.d(source, string + " " + e.getMessage());
        else {
            info(string);
        }
    }
}
