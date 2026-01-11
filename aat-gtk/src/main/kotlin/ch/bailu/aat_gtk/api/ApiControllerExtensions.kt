package ch.bailu.aat_gtk.api

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.api.brouter.BrouterController
import ch.bailu.aat_lib.api.nominatim.NominatimReverseController
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

fun BrouterController.setActions(app: Application) {
    setAction(app, "brouter", { this.onAction() })
}

fun NominatimReverseController.setActions(app: Application) {
    setAction(app, Strings.ACTION_LOCATION_REVERSE) { onAction() }
    setAction(app, Strings.ACTION_LOCATION_REVERSE_CENTER) { center() }
}

private fun setAction(app: Application, action: String, onActivate: ()->Unit) {
    ActionHandler.get(app, action).apply {
        disconnectSignals()
        onActivate(onActivate)
    }
}
