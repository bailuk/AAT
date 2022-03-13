package ch.bailu.aat_gtk.view.description

import ch.bailu.aat_gtk.view.LabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation

class DescriptionLabelTextView(private val description: ContentDescription)
    : LabelTextView(description.label), OnContentUpdatedInterface {

    init {
        text = description.valueAsString
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        description.onContentUpdated(iid, info)
        text = description.valueAsString
    }
}