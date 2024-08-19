package ch.bailu.aat_lib.service.tileremover

import ch.bailu.foc.Foc

class TileFile(
    val source: Int,
    private val zoom: Short,
    private val x: Int,
    private val y: Int,
    file: Foc
) {

    companion object {
        @Throws(NumberFormatException::class)
        fun getY(file: Foc): Int {
            val name = file.name
            val yName = name.substring(0, name.length - 4)
            return yName.toInt()
        }
    }

    private val age: Long
    private val size: Long

    init {
        age = file.lastModified()
        size = file.length()
    }

    constructor(summary: Int, zoom: Short, x: Int, file: Foc) : this(
        summary,
        zoom,
        x,
        getY(file),
        file
    )

    fun toFile(base_dir: Foc): Foc {
        return base_dir.child(toString())
    }

    override fun toString(): String {
        return zoom.toString() + "/" +
                x +
                "/" +
                y + ".png"
    }

    fun lastModified(): Long {
        return age
    }

    fun length(): Long {
        return size
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        return if (other is TileFile) {
            other.x == x && other.y == y && other.zoom == zoom && other.source == source
        } else false
    }

    override fun hashCode(): Int {
        var result = source
        result = 31 * result + zoom
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + age.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }
}
