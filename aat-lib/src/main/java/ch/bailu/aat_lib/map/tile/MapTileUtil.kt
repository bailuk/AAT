package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.TileBitmap
import java.io.IOException

object MapTileUtil {
    fun loadThrow(file: Foc, size: Int, transparent: Boolean): TileBitmap {
        file.openR()?.use {
            val result = AppGraphicFactory.instance().createTileBitmap(it, size, transparent)
            if (result is TileBitmap) {
                result.timestamp = file.lastModified()
                return result
            }
        }
        throw IOException(file.toString())
    }
}
