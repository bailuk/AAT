package ch.bailu.aat_lib.util.extensions


fun String.ellipsize(max: Int = 30): String {
    if (this.length <= max) return this
    return this.substring(0, max) + "…"
}

fun String.ellipsizeStart(max: Int): String {
    if (this.length > max -1) {
        return "…" + this.substring(this.length - max, this.length)
    }
    return this
}

fun String.escapeUnderscore(): String {
    return replace("_", "__")
}

fun <E>ArrayList<E>.addUnique(e: E) {
    if (!this.contains(e)) {
        this.add(e)
    }
}

fun <E>ArrayList<E>.getFirstOrDefault(e: E): E {
    if (this.isNotEmpty()) {
        return this[0]
    }
    return e
}
