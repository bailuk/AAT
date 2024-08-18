package ch.bailu.aat_lib.lifecycle

class LifeCycleDispatcher : LifeCycleInterface {
    private val targets = ArrayList<LifeCycleInterface>(10)
    private var awake = false

    override fun onResumeWithService() {
        for (t in targets) t.onResumeWithService()
        awake = true
    }

    override fun onPauseWithService() {
        for (t in targets) t.onPauseWithService()
        awake = false
    }

    override fun onDestroy() {
        for (t in targets) t.onDestroy()
        awake = false
    }

    fun add(t: LifeCycleInterface) {
        targets.add(t)
        if (awake) t.onResumeWithService()
    }
}
