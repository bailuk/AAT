package ch.bailu.aat_gtk.view.description

import ch.bailu.aat_gtk.view.LabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation

class DescriptionLabelTextView(private val description: ContentDescription)
    : LabelTextView(description.getLabel()), TargetInterface {

    init {
        text = description.getValueAsString()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        description.onContentUpdated(iid, info)
        text = description.getValueAsString()
    }
}
