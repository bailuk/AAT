package ch.bailu.aat.activities

import ch.bailu.aat.util.NominatimApi
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration

class NominatimActivity : AbsOsmApiActivity() {

    public override fun createApiConfiguration(boundingBox: BoundingBoxE6): OsmApiConfiguration {
        return object : NominatimApi(this@NominatimActivity, boundingBox) {
            override val queryString: String
                get() = editorView.toString()
        }
    }

    public override fun addCustomButtons(bar: MainControlBar) {
        bar.addSpace()
    }
}
