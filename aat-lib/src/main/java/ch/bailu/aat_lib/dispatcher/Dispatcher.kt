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

    private fun addSingleTarget(target: OnContentUpdatedInterface, iid: Int) {
        getTargetList(iid).add(target)
    }

    private fun getTargetList(iid: Int): TargetList {
        return targets.getOrPut(iid) { TargetList() }
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
        updater = enabledUpdater
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

    private val enabledUpdater = object : OnContentUpdatedInterface {
        override fun onContentUpdated(iid: Int,  info: GpxInformation) {
            update(iid, iid, info)
            update(InfoID.ALL, iid, info)
        }

        private fun update(listID: Int, infoID: Int, info: GpxInformation) {
            targets[listID]?.onContentUpdated(infoID, info)
        }
    }
}
