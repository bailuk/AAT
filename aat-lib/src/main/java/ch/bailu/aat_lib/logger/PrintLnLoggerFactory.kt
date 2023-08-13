package ch.bailu.aat_lib.logger

class PrintLnLoggerFactory : LoggerFactory {
    override fun warn(): Logger {
        return PrintLnLogger("WARN")
    }

    override fun info(): Logger {
        return PrintLnLogger("INFO")
    }

    override fun debug(): Logger {
        return PrintLnLogger("DEBUG")
    }

    override fun error(): Logger {
        return PrintLnLogger("ERROR", System.err)
    }
}
