package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID

class Dispatcher : DispatcherInterface, OnContentUpdatedInterface {
    private val targets: MutableMap<Int, TargetList> = HashMap(10)
    private val sources = ArrayList<ContentSourceInterface>(5)
    private var updater = OnContentUpdatedInterface.NULL
    override fun addTarget( target: OnContentUpdatedInterface, vararg iid: Int) {
        for (i in iid) addSingleTarget(target, i)
    }

    private fun addSingleTarget( t: OnContentUpdatedInterface, iid: Int) {
        getTargetList(iid).add(t)
    }

    private fun getTargetList(iid: Int): TargetList {
        if (!targets.containsKey(iid)) {
            targets[iid] = TargetList()
        }
        return targets[iid]!!
    }

    override fun addSource(source: ContentSourceInterface) {
        sources.add(source)
        source.setTarget(this)
    }

    fun onPause() {
        updater = OnContentUpdatedInterface.NULL
        for (source in sources) {
            source.onPause()
        }
    }

    fun onResume() {
        updater = ON
        for (source in sources) {
            source.onResume()
        }
        requestUpdate()
    }

    override fun requestUpdate() {
        for (source in sources) source.requestUpdate()
    }

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        updater.onContentUpdated(iid, info)
    }

    private val ON: OnContentUpdatedInterface = object : OnContentUpdatedInterface {
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
