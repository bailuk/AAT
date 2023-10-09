package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.util.Objects

object BroadcastData {
    @JvmStatic
    fun has(args: Array<out String>, vid: String): Boolean {
        for (arg in args) {
            if (Objects.equals(arg, vid)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun getFile(args: Array<out String>): String {
        return BroadcastData[args, 0]
    }

    @JvmStatic
    fun getUrl(args: Array<out String>): String {
        return BroadcastData[args, 1]
    }

    operator fun get(args: Array<out String>, index: Int): String {
        return if (args.size > index) args[index] else ""
    }
}
