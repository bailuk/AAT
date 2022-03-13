package ch.bailu.aat_gtk.util

import ch.bailu.gtk.type.Str

object EmptyStr : Str("") {
    override fun destroy() {}
}
