package ch.bailu.aat_gtk.ui.util

import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.Glib

object UiThread {
    fun toUi(function: () -> (Unit)) {
        if (isUi()) {
            function()
        } else {
            Glib.idleAdd({
                function()
                GTK.FALSE
            }, null)
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