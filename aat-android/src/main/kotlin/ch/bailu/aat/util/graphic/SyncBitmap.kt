package ch.bailu.aat.util.graphic

import android.graphics.BitmapFactory
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.foc.Foc
import com.caverock.androidsvg.SVG
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.map.android.graphics.AndroidBitmap
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream

class SyncBitmap {
    var bitmap: Bitmap? = null
        private set

    var size = Obj.MIN_SIZE
        private set

    @Synchronized
    @Throws(IOException::class)
    fun set(file: Foc) {
        set(load(file))
    }

    @Synchronized
    fun set(b: Bitmap?) {
        if (bitmap === b) return
        free()
        bitmap = b

        val bitmap = this.bitmap
        size = if (bitmap != null) {
            bitmap.width * bitmap.height + 4
        } else {
            Obj.MIN_SIZE
        }
    }

    @Synchronized
    fun set(size: Int, transparent: Boolean) {
        set(AndroidGraphicFactory.INSTANCE.createTileBitmap(size, transparent))
    }

    @Synchronized
    fun set(svg: SVG?, size: Int) {
        if (svg is SVG) {
            val b = AndroidSyncTileBitmap()
            b.set(svg, size)
            set(b.getTileBitmap())
        }
    }

    @Synchronized
    fun free() {
        bitmap?.decrementRefCount()
        bitmap = null
        size = Obj.MIN_SIZE
    }

    companion object {
        @Throws(IOException::class)
        private fun load(file: Foc): Bitmap {
            val bitmap: android.graphics.Bitmap?

            var inputStream: InputStream? = null
            try {
                inputStream = BufferedInputStream(file.openR())
                bitmap = BitmapFactory.decodeStream(inputStream)
            } finally {
                Foc.close(inputStream)
            }
            if (bitmap == null) throw IOException(inputStream.toString())
            return AndroidBitmap(bitmap)
        }
    }
}
