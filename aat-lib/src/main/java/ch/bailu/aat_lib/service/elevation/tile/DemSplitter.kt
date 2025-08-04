package ch.bailu.aat_lib.service.elevation.tile


open class DemSplitter(private val parent: DemProvider) : DemProvider {
    private val parentDimension = parent.getDimension()
    private val dimension: DemDimension = DemDimension(
        parentDimension.dimension * 2,
        parentDimension.offset * 3
    ) // Add extra offset (1x) for MultiCell. Original (parent) offset (2x) is used by DemSplitter.

    private val cellDistance: Float = parent.getCellDistance() / 2

    override fun getElevation(index: Int): Short {
        val row = index / dimension.dimension
        val col = index % dimension.dimension

        val parentRow = row / 2
        val parentCol = col / 2

        val parentIndex = parentRow * parentDimension.dimension + parentCol

        val rowMode = row % 2
        val colMode = col % 2

        /*
          Kernel:
           a b c
           d E f
           g h i

          Splitted E:
            A B
            C D
         */
        val e = parentIndex
        val b = e - parentDimension.dimension
        val a = b - 1
        val c = b + 1
        val d = e - 1
        val f = e + 1
        val h = e + parentDimension.dimension
        val g = h - 1
        val i = h + 1


        var sum = parent.getElevation(e) * 2
        val div = 12f

        sum = if (rowMode + colMode == 0) { // A
            sum + parent.getElevation(a) * 2 + parent.getElevation(b) * 2 +
                    parent.getElevation(c) + parent.getElevation(d) * 2 +
                    parent.getElevation(f) +
                    parent.getElevation(g) +
                    parent.getElevation(h) // +

            //parent.getElevation(i);
        } else if (rowMode == 0) {    // B
            sum +
                    parent.getElevation(a) + parent.getElevation(b) * 2 + parent.getElevation(c) * 2 +
                    parent.getElevation(d) + parent.getElevation(f) * 2 +  //parent.getElevation(g) +
                    parent.getElevation(h) +
                    parent.getElevation(i)
        } else if (colMode == 0) {    // C
            sum +
                    parent.getElevation(a) +
                    parent.getElevation(b) +  //parent.getElevation(c) +
                    parent.getElevation(d) * 2 +
                    parent.getElevation(f) + parent.getElevation(g) * 2 + parent.getElevation(h) * 2 +
                    parent.getElevation(i)
        } else {                     // D
            sum +  //parent.getElevation(a) +
                    parent.getElevation(b) +
                    parent.getElevation(c) +
                    parent.getElevation(d) + parent.getElevation(f) * 2 +
                    parent.getElevation(g) + parent.getElevation(h) * 2 + parent.getElevation(i) * 2
        }

        return Math.round(sum / div).toShort()
    }

    override fun getDimension(): DemDimension {
        return dimension
    }

    fun getParentDimension(): DemDimension {
        return parentDimension
    }

    override fun getCellDistance(): Float {
        return cellDistance
    }

    override fun hasInverseLatitude(): Boolean {
        return parent.hasInverseLatitude()
    }

    override fun hasInverseLongitude(): Boolean {
        return parent.hasInverseLongitude()
    }

    fun getParent(): DemProvider {
        return parent
    }

    companion object {
        fun factory(dem: DemProvider): DemProvider {
            return if (dem.hasInverseLatitude() && !dem.hasInverseLongitude()) {
                DemSplitterNE(dem)
            } else if (!dem.hasInverseLatitude() && !dem.hasInverseLongitude()) {
                DemSplitterSE(dem)
            } else if (!dem.hasInverseLatitude() && dem.hasInverseLongitude()) {
                DemSplitterSW(dem)
            } else {
                DemSplitterNW(dem)
            }
        }
    }
}
