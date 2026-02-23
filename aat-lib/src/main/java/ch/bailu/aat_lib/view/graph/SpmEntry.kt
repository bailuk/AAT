package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.SampleRate.Companion.getValue

class SpmEntry(
    private val color: Int,
    private val label: String,
    private val unit: String,
    private val maxKey: Int,
    private vararg val keys: Int
) {
    private var plotter: GraphPlotter? = null
    private var summaryDistance = 0f

    constructor(color: Int, description: ContentDescription, maxKey: Int, vararg keys: Int) : this(
        color,
        description.getLabel(),
        description.getUnit(),
        maxKey,
        *keys
    )

    fun setLabelText(labels: LabelInterface) {
        labels.setText(color, label, unit)
    }

    fun setPlotter(kmFactor: Int, canvas: GraphCanvas, width: Int, height: Int) {
        plotter = GraphPlotter(canvas, width, height, (1000 * kmFactor).toFloat())
    }

    fun getPlotter(): GraphPlotter? {
        return plotter
    }

    fun getMax(list: GpxList): Int {
        return list.getDelta().getAttributes().getAsInteger(maxKey)
    }

    fun incrementSummaryDistance(distance: Float) {
        summaryDistance += distance
    }

    fun plotIfDistance(point: GpxPointNode, minDistance: Float, distance: Float) {
        if (summaryDistance >= minDistance) {
            val value = getValue(point.getAttributes(), *keys)

            if (value > 0) {
                //distance += summaryDistance;
                summaryDistance = 0f
                plotter?.plotData(distance, value.toFloat(), color)
            }
        }
    }
}
