package ch.bailu.aat_lib.service.tracker

abstract class State(protected val internal: TrackerInternals) : StateInterface {
    init {
        internal.logger.setState(getStateID())
    }

    open fun preferencesChanged() {}
}
