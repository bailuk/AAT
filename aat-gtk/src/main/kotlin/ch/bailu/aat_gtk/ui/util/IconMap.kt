package ch.bailu.aat_gtk.ui.util

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.bridge.Image as ImageBridge
import ch.bailu.gtk.gdkpixbuf.Pixbuf
import ch.bailu.gtk.gtk.Image

object IconMap {
    private data class IconId (val name: String, val size: Int)

    private val pixbufs = HashMap<IconId, Pixbuf>()
    private val images = HashMap<IconId, Image>()

    fun getPixbuf(name: String, size: Int): Pixbuf {
        try {
            return _getPixbuf(name, size)
        } catch (e: Exception) {
            AppLog.d(this,"Image resource not found: $name")
            return _getPixbuf("none", size)
        }
    }


    private fun _getPixbuf(name: String, size: Int): Pixbuf {
        var result = pixbufs[IconId(name, size)]

        return if (result == null) {
            val input = IconMap.javaClass.getResourceAsStream("/icons/${name}.svg")
            result = ImageBridge.load(input, size, size)
            pixbufs[IconId(name, size)] = result
            result
        } else {
            result
        }
    }

    fun getImage(name: String, size: Int): Image {
        var result = images[IconId(name, size)]

        return if (result == null) {
            result = Image.newFromPixbufImage(getPixbuf(name, size))
            images[IconId(name, size)] = result
            result.ref()
            result
        } else {
            result
        }
    }
}


