package ch.bailu.aat_lib.service.elevation.tile

class DemSplitterSE(p: DemProvider) : DemSplitter(p) {
    override fun getElevation(index: Int): Short {
        val row = index / getDimension().dimension
        val col = index % getDimension().dimension

        val parentRow = row / 2
        val parentCol = col / 2

        val parentIndex = parentRow * getParentDimension().dimension + parentCol

        val rowMode = row % 2
        val colMode = col % 2

        val sum: Float
        val div = 2f

        /*
           a b
           C d

           [0,0] [0,1]
           [1,0] [1,1]

         */
        val A = getParent().getElevation(parentIndex).toInt()
        if (rowMode + colMode == 0) { // a
            sum = (A + A).toFloat()
        } else if (rowMode == 0) {    // b
            val b = getParent().getElevation(parentIndex + 1).toInt()
            sum = (A + b).toFloat()
        } else if (colMode == 0) {    // c
            val c = getParent().getElevation(parentIndex + getParentDimension().dimension).toInt()
            sum = (A + c).toFloat()
        } else {                     // d
            val d =
                getParent().getElevation(parentIndex + getParentDimension().dimension + 1).toInt()
            sum = (A + d).toFloat()
        }

        return Math.round(sum / div).toShort()
    }
}
