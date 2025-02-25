package ch.bailu.aat_gtk.controller

import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileEnabled

class TrackerOverlayOnOffController(storageInterface: StorageInterface, private val dispatcher: Dispatcher): TargetInterface, OnPreferencesChanged {

    private val solidOverlayFileEnabled = SolidOverlayFileEnabled(storageInterface, InfoID.TRACKER)
    private var state = StateID.OFF

    init {
        dispatcher.addTarget(this, InfoID.TRACKER)
        dispatcher.requestUpdate()
        solidOverlayFileEnabled.register(this)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (iid == InfoID.TRACKER) {
            val state = info.getState()

            if (state != this.state) {
                this.state = state
                if (state == StateID.ON) {
                    solidOverlayFileEnabled.value = true
                }
            }
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solidOverlayFileEnabled.hasKey(key)) {
            dispatcher.requestUpdate()
        }
    }
}
