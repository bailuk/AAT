package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.util.EmptyStr
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.gtk.Label as GtkLabel

class Label : GtkLabel(EmptyStr) {
    private var str: Str = EmptyStr

    var text = ""
        set(value) {
            val newStr = Str(value)
            label = newStr
            str.destroy()
            str = newStr
            field = value
        }
}
