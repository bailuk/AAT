package ch.bailu.aat_lib.logger

import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.Broadcaster

class BroadcastLoggerFactory(
    private val broadcaster: Broadcaster,
    private val fallback: LoggerFactory
) : LoggerFactory {
    override fun warn(): Logger {
        return fallback.warn()
    }

    override fun info(): Logger {
        return BroadcastLogger(broadcaster, AppBroadcaster.LOG_INFO, fallback.info())
    }

    override fun debug(): Logger {
        return fallback.debug()
    }

    override fun error(): Logger {
        return BroadcastLogger(
            broadcaster,
            AppBroadcaster.LOG_ERROR,
            fallback.error()
        )
    }
}
