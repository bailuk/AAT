package ch.bailu.aat_lib.logger

import ch.bailu.aat_lib.util.Objects
import java.util.regex.Pattern

/**
 * Logging facade for application scope
 */
object AppLog {
    private const val UNKNOWN = ""
    private val DEFAULT_TAG = AppLog::class.java.simpleName

    private var warn = PrintLnLoggerFactory().warn()
    private var info = PrintLnLoggerFactory().info()
    private var debug = PrintLnLoggerFactory().debug()
    private var error = PrintLnLoggerFactory().error()

    fun set(loggerFactory: LoggerFactory) {
        warn = loggerFactory.warn()
        info = loggerFactory.info()
        debug = loggerFactory.debug()
        error = loggerFactory.error()
    }


    @JvmStatic
    fun i(source: Any?, message: String?) {
        info.log(toSaveSourceName(source), Objects.toString(message))
    }

    /**
     * Log a message with log level error
     * @param source to log the source: content of String or class name of Any
     * @param throwable this error will be logged. Can be null
     */
    @JvmStatic
    fun e(source: Any?, throwable: Throwable?) {
        e(toSaveSourceName(source), toStringAndPrintStackTrace(throwable))
    }

    /**
     * Log a message with log level error
     * @param source to log the source: content of String or class name of Any
     * @param throwable to print stack trace
     * @param message message to log
     */
    fun e(source: Any, throwable: Throwable, message: String) {
        e(source, message)
        throwable.printStackTrace()
    }


    /**
     * Log a message with log level error
     * @param source that gets logged to identify the source of this message. Can be null
     * @param message the message that gets logged. Can be null
     */
    @JvmStatic
    fun e(source: Any?, message: String?) {
        error.log(toSaveSourceName(source), Objects.toString(message))
    }

    /**
     * Log a message with log level warning
     * @param source The class name of this object will be displayed in the log as a tag to identify the source of this message.
     * @param throwable this error will be logged. Can be null
     */
    fun w(source: Any?, throwable: Throwable?) {
        w(source, toStringAndPrintStackTrace(throwable))
    }

    /**
     *
     * Log a message with log level warning.
     * Message gets logged to Android's internal logger
     * and is not visible to the user.
     * @param source The class name of this object will be displayed in the log as a tag to identify the source of this message.
     * @param message The message that gets logged. This parameter is null save.
     */
    @JvmStatic
    fun w(source: Any?, message: String?) {
        warn.log(toSaveSourceName(source), Objects.toString(message))
    }

    /**
     * Log a message with log level debug
     * @param source classname of object is used as a tag to identify the source of this message.
     * @param message the message to log. Can be null.
     */
    fun d(source: Any?, message: String?) {
        debug.log(toSaveSourceName(source), Objects.toString(message))
    }

    private fun toSaveSourceName(source: Any?): String {
        var result = DEFAULT_TAG
        if (source is String) {
            result = source
        } else if (source != null) {
            result = source.javaClass.simpleName
            if (result.isEmpty()) {
                result = classNameInnerClass(source)
            }
        }
        return result
    }

    private fun classNameInnerClass(source: Any): String {
        var result = source.javaClass.name
        if (result.contains(".")) {
            val parts = result.split(Pattern.quote(".").toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            result = parts[parts.size - 1]
        }
        return result
    }

    private fun toStringAndPrintStackTrace(throwable: Throwable?): String {
        var result: String = UNKNOWN
        if (throwable != null) {
            throwable.printStackTrace()
            if (throwable.localizedMessage != null) {
                result += throwable.localizedMessage
            } else if (throwable.message != null) {
                result += throwable.message
            }
        }
        return result
    }
}
