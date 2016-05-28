package ch.bailu.aat.services.tracker;

import ch.bailu.aat.activities.TrackerActivity;

import ch.bailu.aat.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

public class StatusIcon  {
    private final static int MY_ID=5;
    
    private Service service;
    private Notification pauseNotification, onNotification, autoPauseNotification;
    
    
    public StatusIcon(Service s) {
        PendingIntent intent;
        
        service=s;
      
        intent = createShowActivityIntent();
        pauseNotification=createNotification(intent, R.string.status_paused);
        onNotification=createNotification(intent, R.string.on);
        autoPauseNotification=createNotification(intent, R.string.status_autopaused);
        
    }
    
    private PendingIntent createShowActivityIntent() {
        Intent intent = new Intent(service, TrackerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(service, 0, intent, 0);
    }


    @SuppressWarnings("deprecation")
    private Notification createNotification(PendingIntent intent, int status_id) {
        String appName = service.getString(R.string.app_name);
        String appInfo = service.getString(status_id);

        Notification notification=new Notification(R.drawable.status,appInfo, System.currentTimeMillis());
        
        notification.setLatestEventInfo(service, appName, appInfo, intent);
        notification.flags |= Notification.FLAG_NO_CLEAR;
        
        return notification;
    }

    public void showAutoPause() {
        service.startForeground(MY_ID, autoPauseNotification);
    }
    
    public void showPause() {
        service.startForeground(MY_ID, pauseNotification);
    }
    
    
    public void showOn() {
        service.startForeground(MY_ID, onNotification);
    }
    
    
    public void hide() {
        service.stopForeground(true);
    }
}
