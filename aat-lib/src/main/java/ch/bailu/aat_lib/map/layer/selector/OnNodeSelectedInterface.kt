package ch.bailu.aat_lib.map.layer.selector

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode

interface OnNodeSelectedInterface {
    fun onNodeSelected(
        iid: Int,
        info: GpxInformation,
        node: GpxPointNode,
        index: Int
    )
}
