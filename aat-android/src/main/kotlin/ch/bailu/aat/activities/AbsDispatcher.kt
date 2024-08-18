package ch.bailu.aat.activities

import android.os.Bundle
import ch.bailu.aat_lib.lifecycle.LifeCycleDispatcher
import ch.bailu.aat_lib.dispatcher.ContentSourceInterface
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.lifecycle.LifeCycleInterface
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.InfoID

abstract class AbsDispatcher : AbsServiceLink(), DispatcherInterface {
    private var dispatcher: Dispatcher? = null
    private var lifeCycle: LifeCycleDispatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatcher = Dispatcher()
        lifeCycle = LifeCycleDispatcher()
    }

    fun addLifeCycle(t: LifeCycleInterface) {
        lifeCycle?.add(t)
    }

    fun addTarget(target: OnContentUpdatedInterface) {
        addTarget(target, InfoID.ALL)
    }

    override fun addTarget(target: OnContentUpdatedInterface, vararg iid: Int) {
        dispatcher?.addTarget(target, *iid)
    }

    override fun addSource(source: ContentSourceInterface) {
        dispatcher?.addSource(source)
    }

    override fun requestUpdate() {
        dispatcher?.requestUpdate()
    }

    override fun onResumeWithService() {
        lifeCycle?.onResumeWithService()
        dispatcher?.onResume()
        super.onResumeWithService()
    }

    override fun onPauseWithService() {
        lifeCycle?.onPauseWithService()
        dispatcher?.onPause()
    }

    override fun onDestroy() {
        lifeCycle?.onDestroy()
        lifeCycle = null
        dispatcher = null
        super.onDestroy()
    }
}
