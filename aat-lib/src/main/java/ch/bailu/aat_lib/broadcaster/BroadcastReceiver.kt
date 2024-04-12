package ch.bailu.aat_lib.broadcaster

fun interface BroadcastReceiver {
    fun onReceive(vararg args: String)
}
