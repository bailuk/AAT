package ch.bailu.aat.activities

import android.os.Bundle
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.lifecycle.LifeCycleDispatcher
import ch.bailu.aat_lib.lifecycle.LifeCycleInterface

abstract class AbsDispatcher : AbsServiceLink() {
    var dispatcher: Dispatcher = Dispatcher()
        private set

    private var lifeCycle: LifeCycleDispatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatcher = Dispatcher()
        lifeCycle = LifeCycleDispatcher()
    }

    fun addLifeCycle(t: LifeCycleInterface) {
        lifeCycle?.add(t)
    }

    override fun onResumeWithService() {
        lifeCycle?.onResumeWithService()
        dispatcher.onResumeWithService()
        super.onResumeWithService()
    }

    override fun onPauseWithService() {
        lifeCycle?.onPauseWithService()
        dispatcher.onPauseWithService()
    }

    override fun onDestroy() {
        lifeCycle?.onDestroy()
        lifeCycle = null
        dispatcher = Dispatcher()
        super.onDestroy()
    }
}
