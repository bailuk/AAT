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
