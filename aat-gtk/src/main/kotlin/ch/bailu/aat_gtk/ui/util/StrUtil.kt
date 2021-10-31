package ch.bailu.aat_gtk.ui.util

import ch.bailu.gtk.type.Str

object StrUtil {
    private object EmptyStr : Str("") {
        override fun destroy() {
            // do nothing
        }
    }

    val emptyStr: Str = EmptyStr
}