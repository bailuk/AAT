package ch.bailu.aat_lib.api

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayInterface
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException

abstract class ApiConfiguration(overlay: SolidOverlayInterface) : Api(overlay) {

    @Throws(UnsupportedEncodingException::class)
    abstract fun getUrl(query: String, bounding: BoundingBoxE6): String
    abstract val urlStart: String
    abstract fun getUrlPreview(query: String, bounding: BoundingBoxE6): String

    abstract fun startTask(appContext: AppContext, boundingBoxE6: BoundingBoxE6)

    val queryFile: Foc
        get() {
            return baseDirectory.child("query.txt")
        }

    val baseDirectory: Foc
        get() {
            val parent = resultFile.parent()
            if (parent is Foc) {
                return parent
            }
            return Foc.FOC_NULL
        }
}
