package ch.bailu.aat_lib.logger

import ch.bailu.aat_lib.broadcaster.Broadcaster

class BroadcastLogger(
    private val broadcaster: Broadcaster,
    private val id: String,
    private val console: Logger
) : Logger {

    override fun log(tag: String, msg: String) {
        console.log(tag, msg)
        broadcaster.broadcast(id, tag, msg)
    }
}
