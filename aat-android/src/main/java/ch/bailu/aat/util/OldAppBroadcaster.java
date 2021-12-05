package ch.bailu.aat.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import ch.bailu.foc.Foc;

public class OldAppBroadcaster {



    public static void broadcast(Context context, String action) {
        Intent intent=new Intent();
        intent.setAction(action);


        context.sendBroadcast(intent);
    }

    public static void register(Context context, BroadcastReceiver receiver, String action) {
        IntentFilter  filter = new IntentFilter(action);
        context.registerReceiver(receiver, filter);
    }


    public static void broadcast(Context context, String action, Foc file) {
        broadcast(context, action, file.getPath());
    }


    public static void broadcast(Context context, String action, String file) {

        Intent intent = new Intent();
        intent.setAction(action);

        AppIntent.setFile(intent, file);
        context.sendBroadcast(intent);
    }


    public static void broadcast(Context context, String action, Foc file, String url) {
        broadcast(context, action, file.getPath(), url);
    }
    public static void broadcast(Context context, String action, String file, String url) {

        Intent intent = new Intent();
        intent.setAction(action);

        AppIntent.setFile(intent, file);
        AppIntent.setUrl(intent, url);

        context.sendBroadcast(intent);
    }


}
