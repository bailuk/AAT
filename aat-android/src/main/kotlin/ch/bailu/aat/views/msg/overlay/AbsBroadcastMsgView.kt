package ch.bailu.aat.views.msg.overlay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ch.bailu.aat.dispatcher.AndroidBroadcaster.Companion.register
import ch.bailu.aat.views.msg.AbsMsgView

abstract class AbsBroadcastMsgView internal constructor(context: Context, private val broadcastMessage: String) : AbsMsgView(context) {

    private val onMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            this@AbsBroadcastMsgView.set(intent)
        }
    }

    override fun attach() {
        register(context, onMessage, broadcastMessage)
    }

    override fun detach() {
        context.unregisterReceiver(onMessage)
    }

    abstract fun set(intent: Intent)
}
