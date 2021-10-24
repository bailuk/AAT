package ch.bailu.aat_gtk.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.HashMap


object SL4JCache {
    private val loggers: MutableMap<String, Logger> = HashMap()

    operator fun get(c: Class<*>): Logger {
        return get(c.name)
    }

    operator fun get(name: String): Logger {
        var result = loggers[name]

        return if (result == null) {
            result = LoggerFactory.getLogger(name)
            loggers[name] = result
            result
        } else {
            result
        }
    }
}
