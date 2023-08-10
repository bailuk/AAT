package ch.bailu.aat_lib.service.tracker

abstract class State(protected val internal: TrackerInternals) : StateInterface {
    init {
        internal.logger.state = getStateID()
    }

    open fun preferencesChanged() {}
}
