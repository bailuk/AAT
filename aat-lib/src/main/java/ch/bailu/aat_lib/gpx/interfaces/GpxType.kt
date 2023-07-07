package ch.bailu.aat_lib.gpx.interfaces

import javax.annotation.Nonnull

enum class GpxType {
    WAY, ROUTE, TRACK, NONE;

    @Nonnull
    override fun toString(): String {
        return name
    }

    fun toInteger(): Int {
        return ordinal
    }

    companion object {
        fun toStrings(): Array<String> {
            val result = ArrayList<String>()
            values().forEach { result.add(it.toString()) }
            return result.toTypedArray()
        }

        @JvmStatic
        fun fromInteger(id: Int): GpxType {
            var checkedId = id
            if (checkedId < 0 || checkedId >= values().size) checkedId = values().size - 1
            return values()[checkedId]
        }
    }
}
