package ch.bailu.aat.util.ui;

import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.util.AppBroadcaster;


public class AppLog  {
    private final static String UNKNOWN = "";

    private final static String NAME_SPACE= AppBroadcaster.NAME_SPACE;
    /**
     * Info level namespace for message broadcaster
     */
    public final static String LOG_INFO = NAME_SPACE + "LOG_INFO";

    /**
     * Error level namespace for message broadcaster
     */
    public final static String LOG_ERROR = NAME_SPACE + "LOG_ERROR";

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
     * @param object to log the class name of this object
     * @param throwable this error will be logged. Can be null
     */
    public static void e(@NotNull Context context, @NotNull Object object, Throwable throwable) {
        e(context, object.getClass().getSimpleName(), toStringAndPrintStackTrace(throwable));
    }


    /**
     * Log a message with log level warning
     * @param object The class name of this object will be displayed in the log as a tag to identify the source of this message.
     * @param throwable this error will be logged. Can be null
     */
    public static void w(@NotNull Object object, Throwable throwable) {
        w(object, toStringAndPrintStackTrace(throwable));
    }


    /**
     *
     * Log a message with log level warning.
     * Message gets logged to Android's internal logger
     * and is not visible to the user.
     * @param object The class name of this object will be displayed in the log as a tag to identify the source of this message.
     * @param msg The message that gets logged. This parameter is null save.
     */
    public static void w(@NotNull Object object, String msg) {
        _w(object.getClass().getSimpleName(), msg);
    }


    private static void _w(String a, String b) {
        android.util.Log.w(toSaveString(a), toSaveString(b));

    }


    public static void e(Context c, String m) {
        sendBroadcast(LOG_ERROR, c, toSaveString(m));
    }


    /**
     * Log a message with log level error
     * @param context needed to broadcast the error
     * @param tag that gets logged to identify the source of this message. Can be null
     * @param msg the message that gets logged. Can be null
     */
    public static void e(@NotNull  Context context, String tag, String msg) {
        sendBroadcast(LOG_ERROR, context, toSaveString(tag), toSaveString(msg));
    }


    /**
     * Log a message with log level debug
     * @param o classname of object is used as a tag to identify the source of this message.
     * @param m the message to log. Can be null.
     */
    public static void d(@NotNull  Object o, String m) {
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

    /**
     * Log a message with log level debug
     * @param tag to identify the source of this message. Can be null.
     * @param msg the message to log. Can be null.
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(toSaveString(tag), toSaveString(msg));
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
