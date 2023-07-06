package ch.bailu.aat.map

import ch.bailu.aat.map.mapsforge.MapsForgeViewBase
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.map.android.graphics.AndroidGraphicFactory

object To {
    @JvmStatic
    fun view(map: MapViewInterface?): MapsForgeViewBase? {
        return if (map is MapsForgeViewBase) {
            return map
        } else {
            null
        }
    }

    fun androidBitmap(b: Bitmap): android.graphics.Bitmap {
        return AndroidGraphicFactory.getBitmap(b)
    }

    fun androidCanvas(c: Canvas): android.graphics.Canvas {
        return AndroidGraphicFactory.getCanvas(c)
    }

    fun androidRect(rect: Rect, cache: android.graphics.Rect): android.graphics.Rect {
        cache.bottom = rect.bottom
        cache.top = rect.top
        cache.left = rect.left
        cache.right = rect.right
        return cache
    }

    @JvmStatic
    fun androidPaint(p: Paint): android.graphics.Paint {
        return AndroidGraphicFactory.getPaint(p)
    }
}
