package ch.bailu.aat_lib.service.elevation.tile

class DemSplitterSW(p: DemProvider) : DemSplitter(p) {
    override fun getElevation(index: Int): Short {
        val row = index / getDimension().dimension
        val col = index % getDimension().dimension

        val parent_row = row / 2
        val parent_col = col / 2

        val parent_index = parent_row * getParentDimension().dimension + parent_col

        val row_mode = row % 2
        val col_mode = col % 2

        val sum: Float
        val div = 2f

        /*
           a b
           C d

           [0,0] [0,1]
           [1,0] [1,1]

         */
        val B = getParent().getElevation(parent_index).toInt()
        if (row_mode + col_mode == 0) { // a
            val a = getParent().getElevation(parent_index - 1).toInt()
            sum = (B + a).toFloat()
        } else if (row_mode == 0) {    // b
            sum = (B + B).toFloat()
        } else if (col_mode == 0) {    // c
            val c =
                getParent().getElevation(parent_index + getParentDimension().dimension - 1).toInt()
            sum = (B + c).toFloat()
        } else {                     // d
            val d = getParent().getElevation(parent_index + getParentDimension().dimension).toInt()
            sum = (B + d).toFloat()
        }

        return Math.round(sum / div).toShort()
    }
}
