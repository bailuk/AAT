package ch.bailu.aat.services.tracker;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.TrackerActivity;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;

public class StatusIcon  {
    private final static int MY_ID=5;
    
    private final ServiceContext scontext;
    private final Notification pauseNotification;
    private final Notification onNotification;
    private final Notification autoPauseNotification;
    
    
    public StatusIcon(ServiceContext s) {
        PendingIntent intent;
        
        scontext=s;
      
        intent = createShowActivityIntent();
        pauseNotification=createNotification(intent, R.string.status_paused);
        onNotification=createNotification(intent, R.string.on);
        autoPauseNotification=createNotification(intent, R.string.status_autopaused);
        
    }
    
    private PendingIntent createShowActivityIntent() {
        Intent intent = new Intent(scontext.getContext(), TrackerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(scontext.getContext(), 0, intent, 0);
    }


    private Notification createNotification(PendingIntent intent, int status_id) {
        if (Build.VERSION.SDK_INT < 11) {
            return createNotificationSDK1(intent, status_id);

        } else if (Build.VERSION.SDK_INT < 16) {
            return createNotificationSDK11(intent, status_id);

        } else if (Build.VERSION.SDK_INT < 21){
            return createNotificationSDK16(intent, status_id);
        } else {
            return createNotificationSDK21(intent, status_id);
        }
    }


    @SuppressWarnings("deprecation")
    private Notification createNotificationSDK1(PendingIntent intent, int status_id) {
        String appName = scontext.getContext().getString(R.string.app_name);
        String appInfo = scontext.getContext().getString(status_id);

        Notification notification=new Notification(R.drawable.status,appInfo, System.currentTimeMillis());
        
        setLatestEventInfoSDK1(notification, scontext.getContext(), appName, appInfo, intent);
        notification.flags |= Notification.FLAG_NO_CLEAR;
        
        return notification;
    }




    private void setLatestEventInfoSDK1(Notification notification,
                                        Context context,
                                        String appName,
                                        String appInfo, PendingIntent intent) {

        try {
            Method deprecatedMethod = notification.getClass().getMethod(
                            "setLatestEventInfo",
                            Context.class,
                            CharSequence.class,
                            CharSequence.class,
                            PendingIntent.class);
            deprecatedMethod.invoke(notification, context, appName, appInfo, intent);
        } catch (NoSuchMethodException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException e) {
            AppLog.e(context, "Missing setLatestEventInfo(...)", e);
        }

    }


    @SuppressWarnings("deprecation")
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
                .setSmallIcon(R.drawable.icon_status)
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
                .setSmallIcon(R.drawable.icon_status)
                .setColor(AppTheme.getHighlightColor())
                .setContentTitle(appName)
                .setContentText(appInfo);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }

    public void showAutoPause() {
        
        try {
            scontext.getService().startForeground(MY_ID, autoPauseNotification);
        } catch (ServiceNotUpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void showPause() {
        try {
            scontext.getService().startForeground(MY_ID, pauseNotification);
        } catch (ServiceNotUpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void showOn() {
        try {
            scontext.getService().startForeground(MY_ID, onNotification);
        } catch (ServiceNotUpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void hide() {
        try {
            scontext.getService().stopForeground(true);
        } catch (ServiceNotUpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
