package ch.bailu.aat_gtk.map

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.util.Rect
import ch.bailu.foc.Foc
import ch.bailu.gtk.cairo.Surface
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.map.gtk.graphics.GtkBitmap


class GtkSyncTileBitmap : MapTileInterface {

    private var bitmap: TileBitmap? = null
    private var size: Int = Obj.MIN_SIZE


    @Synchronized
    override fun isLoaded(): Boolean {
        return bitmap != null
    }

    @Synchronized
    override fun set(tileBitmap: TileBitmap?) {
        if (bitmap == tileBitmap) return

        free()
        bitmap = tileBitmap
        size = getSizeOfBitmap()
    }


    private fun getSizeOfBitmap(): Int {
        val tileBitmap = bitmap

        return if (tileBitmap is TileBitmap) {
            (tileBitmap.height * tileBitmap.width * 4)
        } else {
            Obj.MIN_SIZE
        }
    }

    @Synchronized
    override fun set(file: Foc, size: Int, transparent: Boolean) {
        set(load(file, size, transparent))
    }

    @Synchronized
    override fun set(size: Int, transparent: Boolean) {
        set(AppGraphicFactory.instance().createTileBitmap(size, transparent))
    }

    private fun load(file: Foc, size: Int, transparent: Boolean): TileBitmap? {
        var result: TileBitmap? = null
        file.openR()?.use {
            result = AppGraphicFactory.instance().createTileBitmap(it, size, transparent)
            result?.timestamp = file.lastModified()
        }
        return result
    }

    @Synchronized
    override fun free() {
        val tileBitmap = bitmap

        if (tileBitmap is TileBitmap) {
            tileBitmap.decrementRefCount()
        }
        bitmap = null
        size = Obj.MIN_SIZE
    }

    @Synchronized
    override fun getTileBitmap(): TileBitmap? {
        return bitmap
    }

    @Synchronized
    override fun getSize(): Long {
        return size.toLong()
    }

    override fun getCanvas(): Canvas {
        val canvas = AppGraphicFactory.instance().createCanvas()
        if (tileBitmap is TileBitmap) {
            canvas.setBitmap(tileBitmap)
        }
        return canvas
    }

    @Synchronized
    override fun setBuffer(src: IntArray, srcRect: Rect) {
        initBitmap()

        val b = bitmap
        if (b is GtkBitmap) {
            b.surface.flush()
            setPixels(b.surface, src, srcRect)
            b.surface.markDirty()
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
        var srcPixel = srcLine * srcPixelsPerLine

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
        if (bitmap == null) {
            set(SolidTileSize.DEFAULT_TILESIZE, true)
        }
    }

}
