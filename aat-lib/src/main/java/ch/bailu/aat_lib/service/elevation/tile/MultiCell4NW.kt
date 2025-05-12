package ch.bailu.aat_lib.service.elevation.tile

class MultiCell4NW(private val demProvider: DemProvider) : MultiCell() {
    /*
     *      a  b
     *      c  D
     */
    private var a: Short = 0
    private var b: Short = 0
    private var c: Short = 0
    private var d: Short = 0
    private var dzx = 0
    private var dzy = 0

    private val dim = demProvider.dim.DIM
    private val totalCellSize = Math.round(demProvider.cellsize * 4f)

    override fun set(x: Int) {
        setCell(x)
        setDeltaZX()
        setDeltaZY()
    }

    private fun setCell(x: Int) {
        val b = x - dim
        val a = b - 1
        val c = x - 1
        val d = x
        this.a = demProvider.getElevation(a)
        this.b = demProvider.getElevation(b)
        this.c = demProvider.getElevation(c)
        this.d = demProvider.getElevation(d)
    }

    override fun deltaZX(): Int {
        return dzx
    }

    override fun deltaZY(): Int {
        return dzy
    }

    private fun setDeltaZX() {
        val sum = ((b + d) - (a + c))
        dzx = (sum * 100) / totalCellSize
    }

    private fun setDeltaZY() {
        val sum = ((c + d) - (b + a))
        dzy = (sum * 100) / totalCellSize
    }
}
