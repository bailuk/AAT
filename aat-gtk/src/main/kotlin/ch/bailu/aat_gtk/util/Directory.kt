package ch.bailu.aat_gtk.util

import ch.bailu.aat_lib.logger.AppLog

object Directory {
    fun openExternal(path: String) {
        try {
            val runtime = Runtime.getRuntime()
            runtime.exec("xdg-open $path")

        } catch (e: Exception) {
            AppLog.i(this, "Failed to open $path")
        }
    }
}
