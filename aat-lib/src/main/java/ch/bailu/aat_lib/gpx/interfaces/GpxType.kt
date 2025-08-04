package ch.bailu.aat_lib.gpx.interfaces

enum class GpxType {
    WAY, ROUTE, TRACK, NONE;


    override fun toString(): String {
        return name
    }

    fun toInteger(): Int {
        return ordinal
    }

    companion object {
        fun toStrings(): Array<String> {
            val result = ArrayList<String>()
            entries.forEach { result.add(it.toString()) }
            return result.toTypedArray()
        }

        @JvmStatic
        fun fromInteger(id: Int): GpxType {
            var checkedId = id
            if (checkedId < 0 || checkedId >= entries.size) checkedId = entries.size - 1
            return entries[checkedId]
        }
    }
}
