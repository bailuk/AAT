package ch.bailu.aat.dispatcher;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.logger.AppLog;

public class AndroidBroadcaster implements Broadcaster {

    private final Context context;
    private final Map<BroadcastReceiver, android.content.BroadcastReceiver> observers = new HashMap<>(5);

    public AndroidBroadcaster (Context c) {
        this.context = c;
    }


    @Override
    public void broadcast(String action, Object... objects) {
        Intent intent=new Intent();
        intent.setAction(action);

        context.sendBroadcast(intent);
    }

    @Override
    public void register(BroadcastReceiver observer, String action) {
        if (!observers.containsKey(observer)) {
            observers.put(observer, new android.content.BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    observer.onReceive(AppIntent.toArgs(intent));
                }
            });
            OldAppBroadcaster.register(context, observers.get(observer), action);
        } else {
            AppLog.e(this, "Observer was allready registered.");
        }
    }

    @Override
    public void unregister(BroadcastReceiver observer) {
        android.content.BroadcastReceiver receiver = observers.remove(observer);
        if (receiver != null) {
            context.unregisterReceiver(receiver);

        } else {
            AppLog.e(this, "Observer was not registered.");
        }
    }
}
