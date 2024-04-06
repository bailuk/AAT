package ch.bailu.aat_lib.map

interface MapContext {
    fun getMetrics(): MapMetrics
    fun draw(): MapDraw
    fun getSolidKey(): String
    fun getTwoNodes(): TwoNodes
    fun getMapView(): MapViewInterface
}
