package ch.bailu.aat.services.tracker;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.service.tracker.StatusIconInterface;


public final class StatusIcon implements StatusIconInterface {
    private final static int MY_ID=5;

    private final ServiceContext scontext;
    private final Notification pauseNotification;
    private final Notification onNotification;
    private final Notification autoPauseNotification;

    private final UiTheme theme = AppTheme.bar;

    public StatusIcon(ServiceContext s) {
        PendingIntent intent;

        scontext=s;

        intent = createShowActivityIntent();
        pauseNotification=createNotification(intent, R.string.status_paused);
        onNotification=createNotification(intent, R.string.on);
        autoPauseNotification=createNotification(intent, R.string.status_autopaused);

    }

    private PendingIntent createShowActivityIntent() {
        Intent intent = new Intent(scontext.getContext(), ActivitySwitcher.getDefaultCockpit());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(scontext.getContext(), 0, intent, 0);
    }


    private Notification createNotification(PendingIntent intent, int status_id) {
        if (Build.VERSION.SDK_INT < 16) {
            return createNotificationSDK11(intent, status_id);

        } else if (Build.VERSION.SDK_INT < 21){
            return createNotificationSDK16(intent, status_id);
        } else if (Build.VERSION.SDK_INT < 26){
            return createNotificationSDK21(intent, status_id);
        } else {
            return createNotificationSDK26(intent, status_id);
        }
    }


    @TargetApi(11)
    private Notification createNotificationSDK11(PendingIntent intent, int status_id) {
        String appName = scontext.getContext().getString(R.string.app_name);
        String appInfo = scontext.getContext().getString(status_id);

        Notification.Builder builder = new Notification.Builder(scontext.getContext())
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.status)
                .setContentTitle(appName)
                .setContentText(appInfo);

        Notification notification = builder.getNotification();

        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }


    @TargetApi(16)
    private Notification createNotificationSDK16(PendingIntent intent, int status_id) {
        String appName = scontext.getContext().getString(R.string.app_name);
        String appInfo = scontext.getContext().getString(status_id);

        Notification.Builder builder = new Notification.Builder(scontext.getContext())
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.status)
                .setContentTitle(appName)
                .setContentText(appInfo);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }


    @TargetApi(21)
    private Notification createNotificationSDK21(PendingIntent intent, int status_id) {
        String appName = scontext.getContext().getString(R.string.app_name);
        String appInfo = scontext.getContext().getString(status_id);

        Notification.Builder builder = new Notification.Builder(scontext.getContext())
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.status)
                .setColor(theme.getHighlightColor())
                .setContentTitle(appName)
                .setContentText(appInfo);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }

    @TargetApi(26)
    private Notification createNotificationSDK26(PendingIntent intent, int status_id) {
        String appName = scontext.getContext().getString(R.string.app_name);
        String appInfo = scontext.getContext().getString(status_id);

        Notification.Builder builder = new Notification.Builder(scontext.getContext())
                .setChannelId(createNotificationChannelSDK26())
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.status)
                .setColor(theme.getHighlightColor())
                .setContentTitle(appName)
                .setContentText(appInfo);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }


    @TargetApi(26)
    private String createNotificationChannelSDK26() {
        String channelId = StatusIcon.class.getName();
        String channelName = scontext.getContext().getString(R.string.app_name);
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW);

        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);



        NotificationManager notificationManager = (NotificationManager)
                scontext.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.createNotificationChannel(chan);

        return channelId;

    }

    public void showAutoPause() {

        scontext.startForeground(MY_ID, autoPauseNotification);
    }

    public void showPause() {
        scontext.startForeground(MY_ID, pauseNotification);
    }


    public void showOn() {
        scontext.startForeground(MY_ID, onNotification);
    }


    public void hide() {
        scontext.stopForeground(true);
    }
}
