package ch.bailu.aat_lib.service.elevation.tile

class MultiCell8(private val demProvider: DemProvider) : MultiCell() {
    /**
     * a  b  c
     * d [e] f
     * g  h  i
     *
     * only works with double offset mode
     */
    private var A = 0
    private var B = 0
    private var C = 0
    private var D = 0
    private var F = 0
    private var G = 0
    private var H = 0
    private var I = 0
    private var dzx = 0
    private var dzy = 0

    private val dim = demProvider.dim.DIM
    private val totalCellSize = Math.round(demProvider.cellsize * 8f)

    override fun set(e: Int) {
        val f = e + 1
        val h = e + dim
        val i = h + 1
        val g = h - 1

        val d = e - 1
        val b = e - dim
        val c = b + 1
        val a = b - 1

        A = demProvider.getElevation(a).toInt()
        B = demProvider.getElevation(b).toInt()
        C = demProvider.getElevation(c).toInt()
        D = demProvider.getElevation(d).toInt()
        F = demProvider.getElevation(f).toInt()
        G = demProvider.getElevation(g).toInt()
        H = demProvider.getElevation(h).toInt()
        I = demProvider.getElevation(i).toInt()

        dzx = setDeltaZX()
        dzy = setDeltaZY()
    }

    override fun deltaZX(): Int {
        return dzx
    }


    override fun deltaZY(): Int {
        return dzy
    }

    private fun setDeltaZX(): Int {
        val sum = (C + 2 * F + I) - (A + 2 * D + G)
        return (sum * 100) / totalCellSize
    }

    private fun setDeltaZY(): Int {
        val sum = (G + 2 * H + I) - (A + 2 * B + C)
        return (sum * 100) / totalCellSize
    }
}
