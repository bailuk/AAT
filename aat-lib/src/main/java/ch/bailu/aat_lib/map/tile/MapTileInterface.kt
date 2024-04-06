package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.util.Rect
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.TileBitmap
import java.io.IOException

interface MapTileInterface {
    fun isLoaded(): Boolean
    fun set(bitmap: Bitmap?)
    fun set(file: Foc, defaultTileSize: Int, transparent: Boolean)

    @Throws(IOException::class)
    fun setSVG(file: Foc, size: Int, transparent: Boolean)

    fun set(defaultTileSize: Int, transparent: Boolean)
    fun free()
    fun getTileBitmap(): TileBitmap?
    fun getBitmap(): Bitmap?
    fun getSize(): Long
    fun getCanvas(): Canvas?
    fun setBuffer(buffer: IntArray, interR: Rect)
}
