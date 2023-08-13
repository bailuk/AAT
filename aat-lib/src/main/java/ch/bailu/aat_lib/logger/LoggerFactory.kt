package ch.bailu.aat_lib.logger

interface LoggerFactory {
    fun warn(): Logger
    fun info(): Logger
    fun debug(): Logger
    fun error(): Logger
}
