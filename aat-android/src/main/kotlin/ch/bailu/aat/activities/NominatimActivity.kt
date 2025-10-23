package ch.bailu.aat.activities

import ch.bailu.aat_lib.api.nominatim.NominatimApi
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat_lib.api.ApiConfiguration

class NominatimActivity : AbsOsmApiActivity() {

    public override fun createApiConfiguration(): ApiConfiguration {
        return object : NominatimApi(appContext) {
            override val queryString: String
                get() = editorView.toString()
        }
    }

    public override fun addCustomButtons(bar: MainControlBar) {
        bar.addSpace()
    }
}
