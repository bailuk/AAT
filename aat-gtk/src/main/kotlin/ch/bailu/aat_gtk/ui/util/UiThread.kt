package ch.bailu.aat_gtk.ui.util

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.Callback
import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.Glib


object UiThread {
    val emitterID = Callback.EmitterID()

    fun toUi(function: () -> (Unit)) {
        if (isUi()) {
            function()
        } else {
            AppLog.d(this, "not ui")
            Glib.idleAdd({
                function()
                GTK.FALSE
            }, emitterID)
        }
    }

    fun ifUi(function: () -> (Unit)) {
        if (isUi()) {
            function()
        }
    }

    fun isUi(): Boolean {
        return "main" == Thread.currentThread().name
    }
}
