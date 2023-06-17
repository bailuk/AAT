package ch.bailu.aat.services.tracker

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import ch.bailu.aat.R
import ch.bailu.aat.app.ActivitySwitcher.Companion.defaultCockpit
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.service.tracker.StatusIconInterface

class StatusIcon(private val scontext: ServiceContext) : StatusIconInterface {
    private val pauseNotification: Notification
    private val onNotification: Notification
    private val autoPauseNotification: Notification
    private val theme = AppTheme.bar

    init {
        val intent: PendingIntent = createShowActivityIntent()

        pauseNotification = createNotification(intent, R.string.status_paused)
        onNotification = createNotification(intent, R.string.on)
        autoPauseNotification = createNotification(intent, R.string.status_autopaused)
    }

    private fun createShowActivityIntent(): PendingIntent {
        val intent = Intent(scontext.getContext(), defaultCockpit)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(scontext.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotification(intent: PendingIntent, status_id: Int): Notification {
        return if (Build.VERSION.SDK_INT < 26) {
            createNotificationSDK21(intent, status_id)
        } else {
            createNotificationSDK26(intent, status_id)
        }
    }

    private fun createNotificationSDK21(intent: PendingIntent, status_id: Int): Notification {
        val appName = scontext.getContext().getString(R.string.app_name)
        val appInfo = scontext.getContext().getString(status_id)
        val builder = Notification.Builder(scontext.getContext())
            .setContentIntent(intent)
            .setSmallIcon(R.drawable.status)
            .setColor(theme.getHighlightColor())
            .setContentTitle(appName)
            .setContentText(appInfo)
        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        return notification
    }

    @TargetApi(26)
    private fun createNotificationSDK26(intent: PendingIntent, status_id: Int): Notification {
        val appName = scontext.getContext().getString(R.string.app_name)
        val appInfo = scontext.getContext().getString(status_id)
        val builder = Notification.Builder(scontext.getContext())
            .setChannelId(createNotificationChannelSDK26())
            .setContentIntent(intent)
            .setSmallIcon(R.drawable.status)
            .setColor(theme.getHighlightColor())
            .setContentTitle(appName)
            .setContentText(appInfo)
        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        return notification
    }

    @TargetApi(26)
    private fun createNotificationChannelSDK26(): String {
        val channelId = StatusIcon::class.java.name
        val channelName = scontext.getContext().getString(R.string.app_name)
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val notificationManager =
            scontext.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    override fun showAutoPause() {
        scontext.startForeground(MY_ID, autoPauseNotification)
    }

    override fun showPause() {
        scontext.startForeground(MY_ID, pauseNotification)
    }

    override fun showOn() {
        scontext.startForeground(MY_ID, onNotification)
    }

    override fun hide() {
        scontext.stopForeground(true)
    }

    companion object {
        private const val MY_ID = 5
    }
}
