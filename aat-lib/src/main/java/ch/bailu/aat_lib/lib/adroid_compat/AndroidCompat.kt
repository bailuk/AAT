package ch.bailu.aat_lib.lib.adroid_compat

// HashMap.getOrDefault(): Call requires API level 24
fun <K, V> HashMap<K, V>.getOrDefaultApi22(key: K, default: V): V {
    val value = this[key]

    if (value != null) {
        return value
    }
    return default
}
