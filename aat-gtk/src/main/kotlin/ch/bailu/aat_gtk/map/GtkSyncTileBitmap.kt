package ch.bailu.aat_gtk.map

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.util.Rect
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.TileBitmap


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

    private fun load(file: Foc, size: Int, transparent:Boolean): TileBitmap? {
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
    override fun setBuffer(buffer: IntArray?, interR: Rect?) {
        println("GtkSyncTileBitmap::setBuffer")
    }
}
