package ch.bailu.aat_gtk.view.description

import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.LabelHelper
import ch.bailu.gtk.type.Str

class NumberView(private val description: ContentDescription) : OnContentUpdatedInterface {
    private val label  = Label(Str.NULL)
    private val number = Label(Str.NULL)
    private val unit   = Label(Str.NULL)

    val box = Box(Orientation.VERTICAL,5)

    private val margin = 6
    private val ptSize = 30000

    init {
        addView(label, margin, 0)
        addView(number, 0, 0).useMarkup = GTK.TRUE
        addView(unit, 0, margin)

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

    fun updateAllText() {
        LabelHelper.setLabel(number, "<span color=\"#51506b\" weight=\"bold\" size=\"${ptSize}\">${description.value}</span>")
        LabelHelper.setLabel(label, description.labelShort)
        LabelHelper.setLabel(unit, description.unit)
    }
}
