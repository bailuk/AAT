package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.resources.Res

class TrackerStateDescription : StateDescription() {
    override fun getLabel(): String {
        return Res.str().tracker()
    }
}
