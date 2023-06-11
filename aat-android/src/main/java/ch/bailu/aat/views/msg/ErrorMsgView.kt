package ch.bailu.aat.views.msg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.OldAppBroadcaster
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.foc.Foc

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
    }

    override fun attach() {}
    override fun detach() {}
    private fun displayError(intent: Intent) {
        val message = intent.getStringExtra(AppIntent.EXTRA_MESSAGE)
        displayError(message)
    }

    /**
     * Gets an [ch.bailu.aat_lib.service.cache.Obj] from the [ch.bailu.aat_lib.service.cache.CacheService]
     * and displays its exception if it has one
     *
     * @param serviceContext needed to access  [ch.bailu.aat_lib.service.cache.CacheService]
     * @param file           needed to identify the [ch.bailu.aat_lib.service.cache.Obj]
     */
    fun displayError(serviceContext: ServiceContext, file: Foc) {
        serviceContext.insideContext {
            val obj = serviceContext.cacheService.getObject(file.path)
            displayError(obj.exception)
            obj.free()
        }
    }

    /**
     * Get an error message from an exception and display it
     *
     * @param exception that gets displayed
     */
    fun displayError(exception: Exception?) {
        displayError(exceptionToCharSequence(exception))
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
    fun registerReceiver() {
        OldAppBroadcaster.register(context, onMessage, AppBroadcaster.LOG_ERROR)
    }

    /**
     * Stop displaying error logs sent from [ch.bailu.aat_lib.logger.AppLog]
     */
    fun unregisterReceiver() {
        context.unregisterReceiver(onMessage)
    }

    companion object {
        private const val DISPLAY_FOR_MILLIS = 15000
        private val BACKGROUND = Color.rgb(50, 0, 0)
        private const val PADDING = 20
        private fun exceptionToCharSequence(e: Exception?): CharSequence? {
            return if (e == null) {
                null
            } else if (e.localizedMessage != null) {
                e.localizedMessage
            } else if (e.message != null) {
                e.message
            } else {
                e.javaClass.simpleName
            }
        }
    }
}
