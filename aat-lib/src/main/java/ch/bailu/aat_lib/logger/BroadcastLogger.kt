package ch.bailu.aat_lib.logger

import ch.bailu.aat_lib.dispatcher.Broadcaster
import javax.annotation.Nonnull

class BroadcastLogger(
    private val broadcaster: Broadcaster,
    private val id: String,
    private val console: Logger
) : Logger {

    override fun log(@Nonnull tag: String, @Nonnull msg: String) {
        console.log(tag, msg)
        broadcaster.broadcast(id, tag, msg)
    }
}
