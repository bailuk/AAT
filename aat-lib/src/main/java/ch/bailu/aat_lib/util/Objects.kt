package ch.bailu.aat_lib.util

object Objects {
    @JvmStatic
    fun equals(a: Any?, b: Any?): Boolean {
        return if (a != null && b != null) {
            a == b
        } else a === b
    }

    fun toString(o: Any?): String {
        if (o != null) {
            return o.toString()
        }
        return ""
    }

    fun toLong(o: Any?): Long {
        return try {
            toString(o).toLong()
        } catch (e: NumberFormatException) {
            0L
        }
    }

    fun toInt(o: Any?): Int {
        return try {
            toString(o).toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun toFloat(o: Any?): Float {
        return try {
            toString(o).toFloat()
        } catch (e: NumberFormatException) {
            0f
        }
    }

    fun toBoolean(o: Any?): Boolean {
        val s = toString(o)
        var c = 'f'
        if (s.isNotEmpty()) c = s[0]
        return c == 't' || c == 'T'
    }

    fun toArray(list: List<String?>): Array<String> {
        val result = arrayOfNulls<String>(list.size)

        for (i in result.indices) {
            result[i] = toString(list[i])
        }
        return result as Array<String>
    }
}
