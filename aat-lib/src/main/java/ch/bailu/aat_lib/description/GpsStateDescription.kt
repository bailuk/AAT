package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.resources.Res

class GpsStateDescription : StateDescription() {
    override fun getLabel(): String {
        return Res.str().gps()
    }
}
