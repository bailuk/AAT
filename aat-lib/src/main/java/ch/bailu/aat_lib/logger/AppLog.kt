package ch.bailu.aat_lib.logger;

import java.util.regex.Pattern;

import ch.bailu.aat_lib.app.AppConfig;


/**
 * Logging facade for application scope
 */
public class AppLog  {
    private final static String UNKNOWN = "";

    private final static String DEFAULT_TAG=AppLog.class.getSimpleName();

    private static Logger warn = new PrintLnLoggerFactory().warn();
    private static Logger info = new PrintLnLoggerFactory().info();
    private static Logger debug = new PrintLnLoggerFactory().debug();
    private static Logger error = new PrintLnLoggerFactory().error();


    public static void set(LoggerFactory loggerFactory) {
        warn = loggerFactory.warn();
        info = loggerFactory.info();
        debug = loggerFactory.debug();
        error = loggerFactory.error();
    }


    /**
     * Log a message with log level info
     * @param message the message that will be logged. Can be null
     */
    public static void i(String message) {
        info.log(DEFAULT_TAG, toSaveString(message));
    }

    public static void i(Object tag, String message) {
        info.log(toSaveClassName(tag), toSaveString(message));
    }


    /**
     * Log a message with log level error
     * @param throwable error that will be logged. Can be null
     */
    public static void e(Throwable throwable) {
        e(throwable, throwable);
    }


    /**
     * Log a message with log level error
     * @param object to log the class name of this object
     * @param throwable this error will be logged. Can be null
     */
    public static void e(Object object, Throwable throwable) {
        e(toSaveClassName(object), toStringAndPrintStackTrace(throwable));
    }


    /**
     * Log a message with log level warning
     * @param object The class name of this object will be displayed in the log as a tag to identify the source of this message.
     * @param throwable this error will be logged. Can be null
     */
    public static void w(Object object, Throwable throwable) {
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
    public static void w(Object object, String msg) {
        warn.log(toSaveClassName(object), toSaveString(msg));
    }


    public static void e(String m) {
        e(DEFAULT_TAG, m);
    }


    /**
     * Log a message with log level error
     * @param tag that gets logged to identify the source of this message. Can be null
     * @param msg the message that gets logged. Can be null
     */
    public static void e(Object tag, String msg) {
        error.log(toSaveClassName(tag), toSaveString(msg));
    }


    /**
     * Log a message with log level debug
     * @param o classname of object is used as a tag to identify the source of this message.
     * @param m the message to log. Can be null.
     */
    public static void d(Object o, String m) {
        d(toSaveClassName(o), m);
    }


    private static String toSaveClassName(Object o) {
        String result = DEFAULT_TAG;

        if (o instanceof String) {
          result = (String) o;
        } else if (o != null) {
            result = o.getClass().getSimpleName();
            if (result.length() == 0) {
                result = classNameInnerClass(o);
            }
        }
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
        if (!AppConfig.getInstance().isRelease()) {
            debug.log(toSaveTag(tag), toSaveString(msg));
        }
    }


    private static String toSaveTag(String result) {
        if (result== null || result.length()==0) {
            result = DEFAULT_TAG;
        }
        return result;
    }

    private static String toSaveString(String s) {
        if (s == null) return UNKNOWN;
        return s;
    }

    private static String toStringAndPrintStackTrace(Throwable e) {
        String result = UNKNOWN;

        if (e != null) {
            e.printStackTrace();

            if (e.getLocalizedMessage() != null) {
                result += e.getLocalizedMessage();
            } else  if (e.getMessage() != null) {
                result += e.getMessage();
            }
        }
        return result;
    }
}
