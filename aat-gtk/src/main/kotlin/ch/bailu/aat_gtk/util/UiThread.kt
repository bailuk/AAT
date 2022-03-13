package ch.bailu.aat_gtk.util

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.Glib


object UiThread {

    fun toUi(function: () -> (Unit)) {
        if (isUi()) {
            function()
        } else {
            AppLog.d(this, "not ui")
            idleAdd { function() }
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

    fun idleAdd(function: () -> Unit) {
        Glib.idleAdd({
            function()
            GTK.FALSE
        }, null)
    }
}
