package ch.bailu.aat_gtk.view.messages

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation

class MessageOverlay {
    val box = Box(Orientation.VERTICAL, Layout.MARGIN).apply {
        margin(Layout.MARGIN)
        marginBottom = 50
        valign = Align.END
        halign = Align.CENTER
        append(MessageBar(AppBroadcaster.LOG_INFO, "message-info").label)
        append(MessageBar(AppBroadcaster.LOG_ERROR,"message-error").label)
    }
}
