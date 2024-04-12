package ch.bailu.aat_lib.broadcaster

interface Broadcaster {
    fun broadcast(action: String, vararg args: String)
    fun register(action: String, broadcastReceiver: BroadcastReceiver)
    fun unregister(broadcastReceiver: BroadcastReceiver)
}
