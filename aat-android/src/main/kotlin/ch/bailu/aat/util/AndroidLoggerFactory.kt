package ch.bailu.aat.util

import android.util.Log
import ch.bailu.aat_lib.logger.Logger
import ch.bailu.aat_lib.logger.LoggerFactory

class AndroidLoggerFactory : LoggerFactory {
    override fun warn(): Logger {
        return Logger { tag: String, msg: String -> Log.w(tag, msg) }
    }

    override fun info(): Logger {
        return Logger { tag: String, msg: String -> Log.i(tag, msg) }
    }

    override fun debug(): Logger {
        return Logger { tag: String, msg: String -> Log.d(tag, msg) }
    }

    override fun error(): Logger {
        return Logger { tag: String, msg: String -> Log.e(tag, msg) }
    }
}
