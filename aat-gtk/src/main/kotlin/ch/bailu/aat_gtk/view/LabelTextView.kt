package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.lib.extensions.setLabel
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

open class LabelTextView(labelText: String) {

    val layout = Box(Orientation.VERTICAL, 2)

    private val _label = Label(Str.NULL)
    private val _value = Label(Str.NULL)

    private val inverse = true

    var text : String
        get() = _value.text.toString()
        set(t)  { _value.setLabel(if (!inverse) t else "<b>$t</b>")}

    var label : String
        get() = _label.text.toString()
        set(t)  { _label.setLabel(if (inverse) t else "<b>$t</b>")}

    init {
        label = labelText

        _label.wrap = GTK.TRUE
        _label.xalign = 0f
        _label.marginEnd = 10
        _label.marginStart = 10
        _label.marginTop = 10
        _label.useMarkup = GTK.IS(!inverse)


        _value.wrap = GTK.TRUE
        _value.xalign = 0f
        _value.marginEnd = 10
        _value.marginBottom = 10
        _value.marginStart = 10
        _value.useMarkup = GTK.IS(inverse)

        layout.append(_label)
        layout.append(_value)
    }
}
