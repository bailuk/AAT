package ch.bailu.aat_lib.dispatcher

fun interface BroadcastReceiver {
    fun onReceive(vararg args: String)
}
