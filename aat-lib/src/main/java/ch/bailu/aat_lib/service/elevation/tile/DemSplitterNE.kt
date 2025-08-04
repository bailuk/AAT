package ch.bailu.aat_lib.service.elevation.tile

class DemSplitterNE(p: DemProvider) : DemSplitter(p) {
    override fun getElevation(index: Int): Short {
        val row = index / getDimension().dimension
        val col = index % getDimension().dimension

        val parentRow = row / 2
        val parentCol = col / 2

        val parentIndex = parentRow * getParentDimension().dimension + parentCol

        val rowMode = row % 2
        val colMode = col % 2

        val sum: Int
        var div = 2

        val C = getParent().getElevation(parentIndex).toInt()

        if (rowMode + colMode == 0) { // a
            val a = getParent().getElevation(parentIndex - getParentDimension().dimension).toInt()
            sum = C + a
        } else if (rowMode == 0) {    // b
            val b =
                getParent().getElevation(parentIndex - getParentDimension().dimension + 1).toInt()
            sum = C + b
        } else if (colMode == 0) {    // c
            sum = C
            div = 1
        } else {                     // d
            val d = getParent().getElevation(parentIndex + 1).toInt()
            sum = C + d
        }
        return (sum / div).toShort()
    }
}
