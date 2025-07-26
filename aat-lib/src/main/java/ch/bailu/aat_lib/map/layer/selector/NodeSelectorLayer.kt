package ch.bailu.aat_lib.map.layer.selector

import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point

class NodeSelectorLayer(
    services: ServicesInterface,
    s: StorageInterface,
    mc: MapContext,
    pos: Position,
    usageTracker: UsageTrackerInterface
) : AbsNodeSelectorLayer(services, s, mc, pos, usageTracker) {

    private val observers: ArrayList<OnNodeSelectedInterface> = ArrayList()

    fun observe(observer: OnNodeSelectedInterface) {
        observers.add(observer)
    }

    override fun onAttached() {}
    override fun onDetached() {}
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }


    override fun setSelectedNode(iid: Int, info: GpxInformation, node: GpxPointNode, index: Int) {
        for (observer in observers) {
            observer.onNodeSelected(iid, info, node, index)
        }
    }
}
