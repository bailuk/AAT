package ch.bailu.aat_gtk.logger

import ch.bailu.aat_lib.logger.Logger

class SL4JLogger : Logger {
    override fun w(tag: String, msg: String) {
        SL4JCache[tag].warn(msg)
    }

    override fun i(tag: String, msg: String) {
        SL4JCache[tag].info(msg)
    }

    override fun d(tag: String, msg: String) {
        SL4JCache[tag].debug(msg)
    }

    override fun e(tag: String, msg: String) {
        SL4JCache[tag].error(msg)
    }
}
