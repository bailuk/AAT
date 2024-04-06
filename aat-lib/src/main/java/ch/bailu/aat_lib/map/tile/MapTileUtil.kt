package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.TileBitmap

object MapTileUtil {
    fun load(file: Foc, size: Int, transparent: Boolean): TileBitmap? {
        var result: TileBitmap? = null

        try {
            file.openR()?.use {
                result = AppGraphicFactory.instance().createTileBitmap(it, size, transparent)
                result?.timestamp = file.lastModified()
            }
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
        return result
    }
}
