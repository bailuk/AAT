package ch.bailu.aat_lib.gpx.interfaces

enum class GpxType {
    WAY, ROUTE, TRACK;

    override fun toString(): String {
        return name
    }

    fun toInteger(): Int {
        return ordinal
    }

    companion object {
        fun toStrings(): List<String> {
            return entries.map { it.toString() }
        }

        fun fromInteger(id: Int): GpxType {
            var checkedId = id
            if (checkedId < 0 || checkedId >= entries.size) checkedId = entries.size - 1
            return entries[checkedId]
        }
    }
}
