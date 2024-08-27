package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster

class MockBroadcaster: Broadcaster {

    private val observers = HashMap<String, ArrayList<BroadcastReceiver>>()

    override fun broadcast(action: String, vararg args: String) {
        observers[action]?.forEach { it.onReceive(*args) }
    }

    override fun register(action: String, broadcastReceiver: BroadcastReceiver) {
        if (!observers.containsKey(action)) {
            observers[action] = ArrayList()
        }
        observers[action]?.add(broadcastReceiver)
    }

    override fun unregister(broadcastReceiver: BroadcastReceiver) {
        observers.keys.forEach {
            observers[it]?.remove(broadcastReceiver)
        }
    }
}
