package ch.bailu.aat.services.tracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import ch.bailu.aat.R;
import ch.bailu.aat.activities.TrackerActivity;
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


    @SuppressWarnings("deprecation")
    private Notification createNotification(PendingIntent intent, int status_id) {
        String appName = scontext.getContext().getString(R.string.app_name);
        String appInfo = scontext.getContext().getString(status_id);

        Notification notification=new Notification(R.drawable.status,appInfo, System.currentTimeMillis());
        
        notification.setLatestEventInfo(scontext.getContext(), appName, appInfo, intent);
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
