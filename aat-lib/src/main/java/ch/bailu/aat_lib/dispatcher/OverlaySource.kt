package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlay
import ch.bailu.aat_lib.preferences.map.SolidOverlayInterface
import ch.bailu.aat_lib.preferences.map.SolidPoiOverlay

class OverlaySource private constructor(
    context: AppContext,
    private val soverlay: SolidOverlayInterface
) : FileSource(context, soverlay.getIID()) {
    private val onPreferencesChanged = OnPreferencesChanged { _, key ->
        if (soverlay.hasKey(key)) {
            initAndUpdateOverlay()
        }
    }

    init {
        initAndUpdateOverlay()
    }

    private fun initAndUpdateOverlay() {
        setFile(soverlay.getValueAsFile())
        super.setEnabled(soverlay.isEnabled())
    }

    override fun setEnabled(enabled: Boolean) {
        soverlay.setEnabled(enabled)
        super.setEnabled(soverlay.isEnabled())
    }

    override fun onPause() {
        super.onPause()
        soverlay.unregister(onPreferencesChanged)
    }

    override fun onResume() {
        super.onResume()
        soverlay.register(onPreferencesChanged)
        initAndUpdateOverlay()
    }

    companion object {
        fun factoryPoiOverlaySource(context: AppContext): OverlaySource {
            return OverlaySource(context, SolidPoiOverlay(context.dataDirectory))
        }

        fun factoryCustomOverlaySource(context: AppContext, index: Int): OverlaySource {
            return OverlaySource(
                context,
                SolidCustomOverlay(context.storage, context, InfoID.OVERLAY + index)
            )
        }
    }
}
