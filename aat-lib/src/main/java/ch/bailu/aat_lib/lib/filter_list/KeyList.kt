package ch.bailu.aat_lib.lib.filter_list


class KeyList(keys: String = "") {
    companion object {
        const val MIN_KEY_LENGTH: Int = 3
        val REGEX = "[ =.;:/]".toRegex()
    }

    private val keys = ArrayList<String>(10)

    init {
        addKeys(keys)
    }

    fun addKeys(s: String) {
        addKeys(s.split(REGEX).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    private fun addKeys(s: Array<String>) {
        for (k in s) {
            addKey(k)
        }
    }

    private fun addKey(key: String) {
        if (key.length >= MIN_KEY_LENGTH) {
            val k = key.lowercase()
            if (!hasKey(k)) {
                keys.add(k)
            }
        }
    }

    fun hasKey(key: String): Boolean {
        keys.forEach {
            if (it == key.lowercase()) return true
        }
        return false
    }

    fun fits(list: KeyList): Boolean {
        for (k in list.keys) if (!fits(k)) return false
        return true
    }

    private fun fits(k: String): Boolean {
        for (x in keys) {
            if (x.contains(k)) return true
        }
        return false
    }

    val isEmpty: Boolean
        get() = keys.isEmpty()

    fun length(): Int {
        var l = 0
        for (k in keys) {
            l += k.length
        }
        return l
    }

    fun size(): Int {
        return keys.size
    }

    fun getKey(i: Int): String {
        return if (keys.size > i) keys[i]
        else ""
    }
}
