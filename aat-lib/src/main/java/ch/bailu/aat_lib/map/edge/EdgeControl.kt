package ch.bailu.aat_lib.map.edge

class EdgeControl  {
    private val controlBars = ArrayList<EdgeViewInterface>()

    fun add(bar: EdgeViewInterface) {
        bar.hide()
        controlBars.add(bar)
    }

    fun hide() {
        controlBars.forEach { it.hide() }
    }

    fun show(pos: Position) {
        controlBars.forEach {
            if (it.pos() == pos) {
                it.show()
            } else {
                it.hide()
            }
        }
    }
}
