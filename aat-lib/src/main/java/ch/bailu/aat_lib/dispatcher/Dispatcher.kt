package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID

class Dispatcher : DispatcherInterface {
    private val targets: MutableMap<Int, TargetList> = HashMap(10)
    private val sources = ArrayList<SourceInterface>(5)
    private var updater = TargetInterface.NULL

    override fun addTarget(target: TargetInterface, vararg iid: Int) {
        for (i in iid) {
            addSingleTarget(target, i)
        }
    }

    private fun addSingleTarget(t: TargetInterface, iid: Int) {
        getTargetList(iid).add(t)
    }

    private fun getTargetList(iid: Int): TargetList {
        if (!targets.containsKey(iid)) {
            targets[iid] = TargetList()
        }
        return targets[iid]!!
    }

    override fun addSource(source: SourceInterface) {
        sources.add(source)
        source.setTarget(this)
    }

    override fun onPauseWithService() {
        updater = TargetInterface.NULL
        for (source in sources) {
            source.onPauseWithService()
        }
    }

    override fun onResumeWithService() {
        updater = ON
        for (source in sources) {
            source.onResumeWithService()
        }
        requestUpdate()
    }

    override fun onDestroy() {}

    override fun requestUpdate() {
        for (source in sources) source.requestUpdate()
    }

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        updater.onContentUpdated(iid, info)
    }

    private val ON: TargetInterface = object : TargetInterface {
        override fun onContentUpdated(iid: Int,  info: GpxInformation) {
            update(iid, iid, info)
            update(InfoID.ALL, iid, info)
        }

        fun update(listID: Int, infoID: Int, info: GpxInformation) {
            val l = targets[listID]
            l?.onContentUpdated(infoID, info)
        }
    }
}


private class TargetList : TargetInterface {
    private val targets = ArrayList<TargetInterface>(10)
    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        for (target in targets) {
            target.onContentUpdated(iid, info)
        }
    }

    fun add(t: TargetInterface) {
        targets.add(t)
    }
}
