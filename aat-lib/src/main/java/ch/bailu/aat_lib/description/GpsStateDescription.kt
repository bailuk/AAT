package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.resources.Res

/** Displays GPS connection state. */
class GpsStateDescription : StateDescription() {
    override fun getLabel(): String {
        return Res.str().gps()
    }
}
