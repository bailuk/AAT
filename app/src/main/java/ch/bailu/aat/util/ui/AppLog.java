package ch.bailu.aat.util.ui;

import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.util.AppBroadcaster;


public class AppLog  {
    private final static String UNKNOWN = "";

    public final static String NAME_SPACE= AppBroadcaster.NAME_SPACE;
    public final static String LOG_INFO = NAME_SPACE + "LOG_INFO";
    public final static String LOG_E = NAME_SPACE + "LOG_ERROR";

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private final static String EXTRA_SOURCE = "TITLE";

    /**
     * Log a message with log level info
     * @param context android context to broadcast message
     * @param message the message that will be logged. Can be null
     */
    public static void i(@NotNull Context context, String message) {
        sendBroadcast(LOG_INFO, context, toSaveString(message));
    }


    /**
     * Log a message with log level error
     * @param context android context to broadcast message
     * @param throwable error that will be logged. Can be null
     */
    public static void e(@NotNull Context context, Throwable throwable) {
        e(context, toStringAndPrintStackTrace(throwable));
    }


    /**
     * Log a message with log level error
     * @param context android context to broadcast message
     * @param object this will log the class name of this object
     * @param throwable this error will be logged. Can be null
     */
    public static void e(@NotNull Context context, @NotNull Object object, Throwable throwable) {
        e(context, object.getClass().getSimpleName(), toStringAndPrintStackTrace(throwable));
    }


    /**
     * Log a message with log level warning
     * @param object
     * @param throwable
     */
    public static void w(@NotNull Object object, Throwable throwable) {
        w(object, toStringAndPrintStackTrace(throwable));
    }


    /**
     *
     * Log a message with log level warning.
     * Message gets logged to Android's internal logger
     * and is not visible to the user.
     * @param o The class name of this object will be displayed in the log.
     * @param m The message that gets logged. This parameter is null save.
     */
    public static void w(@NotNull Object o, String m) {
        _w(o.getClass().getSimpleName(), m);
    }


    private static void _w(String a, String b) {
        android.util.Log.w(toSaveString(a), toSaveString(b));

    }


    public static void e(Context c, String m) {
        sendBroadcast(LOG_E, c, toSaveString(m));
    }


    public static void e(Context c, String a, String b) {
        sendBroadcast(LOG_E, c, toSaveString(a), toSaveString(b));
    }


    public static void d(Object o, String m) {
    	d(className(o), m);
    }

    private static String className(Object o) {
        String result = o.getClass().getSimpleName();
        if (result.length() == 0)
                result =  classNameInnerClass(o);
        return result;
    }

    private static String classNameInnerClass(Object o) {
        String result = o.getClass().getName();

        if (result.contains(".")) {
            String[] parts = result.split(Pattern.quote("."));
            result = parts[parts.length - 1];
        }
        return result;
    }

    public static void d(String a, String b) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(toSaveString(a), toSaveString(b));
        }
    }


    private static void sendBroadcast(String i, Context c, String m) {
        sendBroadcast(i,c,UNKNOWN, m);
    }


    private static void sendBroadcast(String i, Context c, String s, String m) {
        Intent intent = new Intent(i);
        intent.putExtra(EXTRA_MESSAGE, m);
        intent.putExtra(EXTRA_SOURCE, s);
        c.sendBroadcast(intent);
    }


    private static String toSaveString(String s) {
        if (s == null) return UNKNOWN;
        return s;
    }

    private static String toStringAndPrintStackTrace(Throwable e) {
        if (e == null) {
            return UNKNOWN;
        } else {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }

            if (e.getMessage() == null) {
                return e.getClass().getSimpleName();
            } else {
                return e.getClass().getSimpleName() + ": " + e.getMessage();
            }
        }
    }

}
