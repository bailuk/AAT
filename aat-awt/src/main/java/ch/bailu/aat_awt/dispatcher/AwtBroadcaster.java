package ch.bailu.aat_awt.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;

public class AwtBroadcaster implements Broadcaster {

    private final Map<String, ArrayList<BroadcastReceiver>> signals = new HashMap<>();

    @Override
    public void broadcast(String signal, Object ...objects) {
        ArrayList<BroadcastReceiver> observers = signals.get(signal);

        if (observers != null) {
            for (BroadcastReceiver observer : observers) {
                observer.onReceive(objects);
            }
        }
    }


    @Override
    public void register(BroadcastReceiver observer, String signal) {
        unregister(observer);

        signals.putIfAbsent(signal, new ArrayList<>());
        signals.get(signal).add(observer);
    }


    @Override
    public void unregister(BroadcastReceiver onLocation) {
        for(ArrayList<BroadcastReceiver> observers : signals.values()) {
            observers.remove(onLocation);
        }
    }
}
