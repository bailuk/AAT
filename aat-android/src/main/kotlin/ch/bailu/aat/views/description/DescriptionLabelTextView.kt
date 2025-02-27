package ch.bailu.aat.views.description

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation


class DescriptionLabelTextView(
    context: Context,
    private val description: ContentDescription,
    theme: UiTheme
) : LabelTextView(true, context, description.getLabel(), theme), TargetInterface {
    init {
        setText()
    }

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        description.onContentUpdated(iid, info)
        setText()
    }

    fun setText() {
        setText(description.getValueAsString())
    }
}
