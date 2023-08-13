package ch.bailu.aat_lib.dispatcher

interface Broadcaster {
    fun broadcast(action: String, vararg args: String)
    fun register(broadcastReceiver: BroadcastReceiver, action: String)
    fun unregister(broadcastReceiver: BroadcastReceiver)
}
