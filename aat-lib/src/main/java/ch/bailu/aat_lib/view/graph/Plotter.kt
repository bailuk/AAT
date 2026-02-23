package ch.bailu.aat_lib.view.graph

abstract class Plotter {
    abstract fun plot(canvas: GraphCanvas, config: PlotterConfig)
    abstract fun initLabels(labels: LabelInterface)
    abstract fun setLimit(firstPoint: Int, lastPoint: Int)
}
