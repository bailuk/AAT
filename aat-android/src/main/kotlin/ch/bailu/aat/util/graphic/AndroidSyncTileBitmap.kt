package ch.bailu.aat.util.graphic

import android.graphics.Canvas
import android.graphics.RectF
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.util.Rect
import ch.bailu.foc.Foc
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.CorruptedInputStreamException
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import java.io.IOException
import java.io.InputStream
import javax.annotation.Nonnull

class AndroidSyncTileBitmap : MapTileInterface {
    private var bitmap: Bitmap? = null
    private var size: Long = 0
    override fun getTileBitmap(): TileBitmap? {
        val b = bitmap
        return if (b is TileBitmap) {
            b
        } else null
    }

    override fun getBitmap(): Bitmap? {
        return bitmap
    }

    val androidBitmap: android.graphics.Bitmap?
        get() {
            val b = bitmap
            return if (b != null) {
                AndroidGraphicFactory.getBitmap(b)
            } else null
        }

    val androidCanvas: Canvas?
        get() {
            val b = androidBitmap
            return if (b != null) {
                Canvas(b)
            } else null
        }

    override fun getCanvas(): org.mapsforge.core.graphics.Canvas? {
        val c = androidCanvas
        return if (c != null) {
            AndroidGraphicFactory.createGraphicContext(c)
        } else null
    }

    @Synchronized
    override fun set(file: Foc, defaultTileSize: Int, transparent: Boolean) {
        set(load(file, defaultTileSize, transparent))
    }

    @Throws(IOException::class)
    override fun setSVG(file: Foc, size: Int, transparent: Boolean) {
        var input: InputStream? = null
        try {
            input = file.openR()
            val svg = SVG.getFromInputStream(input)
            set(svg, size)
        } catch (e: SVGParseException) {
            throw IOException(e.message)
        } finally {
            Foc.close(input)
        }
    }

    @Synchronized
    override fun set(b: Bitmap?) {
        if (bitmap === b) return
        free()
        bitmap = b
        size = sizeOfBitmap
    }

    private val sizeOfBitmap: Long
        get() {
            var result = Obj.MIN_SIZE
            val b = androidBitmap
            if (b != null) {
                result = b.rowBytes * b.height
            }
            return result.toLong()
        }

    @Synchronized
    override fun set(defaultTileSize: Int, transparent: Boolean) {
        set(AndroidGraphicFactory.INSTANCE.createTileBitmap(defaultTileSize, transparent))
    }

    @Synchronized
    operator fun set(svg: SVG, size: Int) {
        set(size, true)
        val p = svg.renderToPicture()
        val c = androidCanvas
        c?.drawPicture(p, RectF(0f, 0f, size.toFloat(), size.toFloat()))
    }

    @Synchronized
    override fun getSize(): Long {
        return size
    }

    @Synchronized
    override fun setBuffer(@Nonnull buffer: IntArray, @Nonnull r: Rect) {
        initBitmap()
        val b = androidBitmap
        b?.setPixels(buffer, 0, r.width(), r.left, r.top, r.width(), r.height())
    }

    private fun initBitmap() {
        if (bitmap == null) {
            set(SolidTileSize.DEFAULT_TILESIZE, true)
        }
    }

    @Synchronized
    override fun free() {
        if (bitmap != null) {
            bitmap!!.decrementRefCount()
        }
        bitmap = null
        size = Obj.MIN_SIZE.toLong()
    }

    override fun isLoaded(): Boolean {
        return bitmap != null
    }

    companion object {
        private fun load(file: Foc, size: Int, transparent: Boolean): TileBitmap? {
            var bitmap: TileBitmap?
            var inputStream: InputStream? = null
            try {
                inputStream = file.openR()
                bitmap =
                    AndroidGraphicFactory.INSTANCE.createTileBitmap(inputStream, size, transparent)
                bitmap.timestamp = file.lastModified()
            } catch (e: CorruptedInputStreamException) {
                bitmap = null
            } catch (e: OutOfMemoryError) {
                bitmap = null
            } catch (e: IOException) {
                bitmap = null
            } finally {
                Foc.close(inputStream)
            }
            return bitmap
        }
    }
}
