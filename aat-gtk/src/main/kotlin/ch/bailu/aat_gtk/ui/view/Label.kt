package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.util.StrUtil
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.gtk.Label as GtkLabel

class Label : GtkLabel(StrUtil.emptyStr) {
    private var str = StrUtil.emptyStr

    var text = ""
        set(value) {
            val newStr = Str(value)
            label = newStr
            str.destroy()
            str = newStr
            field = value
        }
}