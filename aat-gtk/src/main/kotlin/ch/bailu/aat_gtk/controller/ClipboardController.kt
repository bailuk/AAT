package ch.bailu.aat_gtk.controller

import ch.bailu.aat_lib.coordinates.LocationParser
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.type.Pointer
import ch.bailu.gtk.type.Str
import org.mapsforge.core.model.LatLong

class ClipboardController(display: Display) {
    private val clipboard = display.clipboard

    fun getText(callback: (String) -> Unit) {
        clipboard.readTextAsync(null, { _self, _, res, _ ->
            val text = clipboard.readTextFinish(res)
            if (text is Str) {
                val string = text.toString()
                if (string.isNotEmpty()) {
                    callback(text.toString())
                }
            }
            text.destroy()
            _self.unregister()
        }, Pointer.NULL)
    }

    fun setText(text: String) {
        clipboard.setText(text)
    }

    fun getLatLong(callback: (LatLong) -> Unit) {
        getText { text ->
            try {
                val latLong = LocationParser.latLongFromString(text)
                callback(latLong)
            } catch (e: Exception) {
                AppLog.e(this, e)
            }
        }
    }
}
