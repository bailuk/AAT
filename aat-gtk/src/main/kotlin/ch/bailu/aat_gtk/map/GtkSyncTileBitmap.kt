package ch.bailu.aat_gtk.map

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.util.Rect
import ch.bailu.foc.Foc
import ch.bailu.gtk.cairo.Surface
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.map.gtk.graphics.GtkBitmap


class GtkSyncTileBitmap : MapTileInterface {

    private var bitmapOrNull: Bitmap? = null
    private var size: Int = Obj.MIN_SIZE


    @Synchronized
    override fun isLoaded(): Boolean {
        return bitmapOrNull != null
    }

    @Synchronized
    override fun set(bitmap: Bitmap?) {
        if (bitmapOrNull == bitmap) return

        free()
        bitmapOrNull = bitmap
        size = getSizeOfBitmap()
    }


    private fun getSizeOfBitmap(): Int {
        val bitmap = bitmapOrNull

        return if (bitmap is Bitmap) {
            (bitmap.height * bitmap.width * 4)
        } else {
            Obj.MIN_SIZE
        }
    }

    @Synchronized
    override fun set(file: Foc, defaultTileSize: Int, transparent: Boolean) {
        set(loadTileBitmap(file, defaultTileSize, transparent))
    }

    @Synchronized
    override fun set(defaultTileSize: Int, transparent: Boolean) {
        set(AppGraphicFactory.instance().createTileBitmap(defaultTileSize, transparent))
    }

    @Synchronized
    override fun setSVG(file: Foc, size: Int, transparent: Boolean) {
        set(loadSVG(file, size))
    }

    private fun loadTileBitmap(file: Foc, size: Int, transparent: Boolean): TileBitmap? {
        var result: TileBitmap? = null
        file.openR()?.use {
            result = AppGraphicFactory.instance().createTileBitmap(it, size, transparent)
            result?.timestamp = file.lastModified()
        }
        return result
    }

    private fun loadSVG(file: Foc, size: Int): Bitmap? {
        var result: Bitmap? = null
        file.openR()?.use {
            result = AppGraphicFactory.instance().createResourceBitmap(it,1f, size, size, 100, 0)
        }
        return result
    }

    @Synchronized
    override fun free() {
        val bitmap = bitmapOrNull

        if (bitmap is Bitmap) {
            bitmap.decrementRefCount()
        }
        bitmapOrNull = null
        size = Obj.MIN_SIZE
    }

    override fun getTileBitmap(): TileBitmap? {
        val bitmap = bitmapOrNull
        if (bitmap is TileBitmap) {
            return bitmap
        }
        return null
    }

    override fun getBitmap(): Bitmap? {
        return bitmapOrNull
    }

    @Synchronized
    override fun getSize(): Long {
        return size.toLong()
    }

    override fun getCanvas(): Canvas {
        val canvas = AppGraphicFactory.instance().createCanvas()
        val bitmap = bitmapOrNull
        if (bitmap is Bitmap) {
            canvas.setBitmap(bitmap)
        }
        return canvas
    }

    @Synchronized
    override fun setBuffer(buffer: IntArray, interR: Rect) {
        initBitmap()

        val bitmap = bitmapOrNull
        if (bitmap is GtkBitmap) {
            bitmap.surface.flush()
            setPixels(bitmap.surface, buffer, interR)
            bitmap.surface.markDirty()
        }
    }

    private fun setPixels(surface: Surface, src: IntArray, srcRect: Rect) {
        val dst = surface.data
        val dstPixelsPerLine = surface.width
        val dstLines = surface.height
        val dstPixelSize = dstPixelsPerLine * dstLines
        val dstPixelOffset = srcRect.left
        val srcPixelsPerLine = srcRect.width()
        val srcPixelSize = src.size

        var srcLine = 0
        var dstLine = srcRect.top
        var dstPixel = dstLine * dstPixelsPerLine + dstPixelOffset
        var srcPixel = 0

        while (dstPixel < dstPixelSize && srcPixel < srcPixelSize) {
            dst.setInt(dstPixel * 4, src[srcPixel])

            dstPixel++
            srcPixel++

            if (dstPixel % dstPixelsPerLine == 0 || srcPixel % srcPixelsPerLine == 0) {
                srcLine++
                dstLine++
                dstPixel = dstLine * dstPixelsPerLine + dstPixelOffset
                srcPixel = srcLine * srcPixelsPerLine
            }
        }
    }

    private fun initBitmap() {
        if (bitmapOrNull == null) {
            set(SolidTileSize.DEFAULT_TILESIZE, true)
        }
    }
}
