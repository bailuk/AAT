package ch.bailu.aat.views.msg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat_lib.dispatcher.AppBroadcaster

class ErrorMsgView(context: Context) : AbsMsgView(context, DISPLAY_FOR_MILLIS) {
    private val onMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            this@ErrorMsgView.displayError(intent)
        }
    }

    init {
        setTextColor(Color.RED)
        setBackgroundColor(BACKGROUND)
        visibility = GONE
        setPadding(PADDING, PADDING, PADDING, PADDING)

        setOnClickListener{
            visibility = GONE
        }
    }

    override fun attach() { registerReceiver() }
    override fun detach() { unregisterReceiver() }

    private fun displayError(intent: Intent) {
        val message = intent.getStringExtra(AppIntent.EXTRA_MESSAGE)
        displayError(message)
    }

    /**
     * Display an error message
     *
     * @param error message that gets displayed
     */
    private fun displayError(error: CharSequence?) {
        if (error.isNullOrEmpty()) {
            set()
        } else {
            set(error.toString())
        }
    }

    /**
     * Receive and display error logs sent from [ch.bailu.aat_lib.logger.AppLog]
     */
    private fun registerReceiver() {
        AndroidBroadcaster.register(context, onMessage, AppBroadcaster.LOG_ERROR)
    }

    /**
     * Stop displaying error logs sent from [ch.bailu.aat_lib.logger.AppLog]
     */
    private fun unregisterReceiver() {
        context.unregisterReceiver(onMessage)
    }

    companion object {
        private const val DISPLAY_FOR_MILLIS = 15000
        private val BACKGROUND = Color.rgb(50, 0, 0)
        private const val PADDING = 20
    }
}
