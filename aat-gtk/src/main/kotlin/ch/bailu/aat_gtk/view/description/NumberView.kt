package ch.bailu.aat_gtk.view.description

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class NumberView(private val description: ContentDescription) : TargetInterface {
    private val label  = Label(Str.NULL)
    private val number = Label(Str.NULL)
    private val unit   = Label(Str.NULL)

    val box = Box(Orientation.VERTICAL,5)

    private val margin = 6

    init {
        addView(label, margin, 0)
        addView(number, 0, 0)
        addView(unit, 0, margin)

        number.addCssClass(Strings.numberView)
        val cssWidth = "${Strings.numberViewWidth}${description.getValue().length}"
        number.addCssClass(cssWidth)
        updateAllText()
    }

    private fun addView(view: Label, marginTop: Int, marginBottom: Int): Label {
        view.xalign = 0f
        view.marginStart = margin
        view.marginTop = marginTop
        view.marginBottom = marginBottom
        box.append(view)
        return view
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        description.onContentUpdated(iid, info)
        updateAllText()
    }

    private fun updateAllText() {
        number.setText(description.getValue())
        label.setText(description.getLabelShort())
        unit.setText(description.getUnit())
    }
}
