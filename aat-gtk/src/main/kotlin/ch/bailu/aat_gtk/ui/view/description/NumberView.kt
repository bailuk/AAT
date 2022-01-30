package ch.bailu.aat_gtk.ui.view.description

import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.LabelHelper
import ch.bailu.gtk.type.Str

class NumberView(val description: ContentDescription) : OnContentUpdatedInterface {
    private val label  = Label(Str.NULL)
    private val number = Label(Str.NULL)
    private val unit   = Label(Str.NULL)

    val box = Box(Orientation.VERTICAL,5)

    init {
        box.append(label)
        box.append(number)
        box.append(unit)

        updateAllText()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        description.onContentUpdated(iid, info)
        updateAllText()
    }

    fun updateAllText() {
        LabelHelper.setLabel(number, description.value)
        LabelHelper.setLabel(label, description.labelShort)
        LabelHelper.setLabel(unit, description.unit)
    }
}
