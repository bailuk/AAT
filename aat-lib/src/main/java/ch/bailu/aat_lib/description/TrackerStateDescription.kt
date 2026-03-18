package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.resources.Res

/** Displays tracker service state. */
class TrackerStateDescription : StateDescription() {
    override fun getLabel(): String {
        return Res.str().tracker()
    }
}
