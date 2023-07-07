package ch.bailu.aat_lib.gpx.attributes

class Keys(vararg keyIndex: Int) {
    private val keys = ArrayList<Int>(10)

    init {
        for (key in keyIndex) {
            add(key)
        }
    }

    fun add(string: String): Int {
        return add(toIndex(string))
    }

    fun add(keyIndex: Int): Int {
        keys.add(keyIndex)
        return keyIndex
    }

    fun hasKey(keyIndex: Int): Boolean {
        for (key in keys) {
            if (key == keyIndex) return true
        }
        return false
    }

    fun size(): Int {
        return keys.size
    }

    fun getKeyIndex(i: Int): Int {
        return keys[i]
    }

    companion object {
        private val indexes = HashMap<String, Int>()
        private val strings = ArrayList<String>(100)
        fun toString(keyIndex: Int): String {
            return strings[keyIndex]
        }

        @JvmStatic
        fun toIndex(string: String): Int {
            // use lower case because xml is case sensitive
            val stringLowerCase = string.lowercase()
            var keyIndex = indexes[stringLowerCase]
            if (keyIndex == null) {
                keyIndex = add(stringLowerCase, stringLowerCase)
            }
            return keyIndex
        }

        private fun add(keyString: String, string: String): Int {
            val keyIndex = strings.size
            strings.add(string)
            indexes[keyString] = keyIndex
            return keyIndex
        }
    }
}
