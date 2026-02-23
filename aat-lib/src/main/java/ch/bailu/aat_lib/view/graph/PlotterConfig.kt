package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList

interface PlotterConfig {
    fun getWidth(): Int
    fun getHeight(): Int
    fun getList(): GpxList
    fun getIndex(): Int
    fun isXLabelVisible(): Boolean
    fun getLabels(): LabelInterface
}
