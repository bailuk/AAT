package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.lifecycle.LifeCycleInterface

interface EmitterInterface : LifeCycleInterface {
    fun requestUpdate()
}
