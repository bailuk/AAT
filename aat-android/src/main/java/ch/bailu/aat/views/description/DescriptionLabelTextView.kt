package ch.bailu.aat.views.description

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.LabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import javax.annotation.Nonnull

class DescriptionLabelTextView(
    context: Context,
    private val description: ContentDescription,
    theme: UiTheme
) : LabelTextView(true, context, description.label, theme), OnContentUpdatedInterface {
    init {
        setText()
    }

    override fun onContentUpdated(iid: Int, @Nonnull info: GpxInformation) {
        description.onContentUpdated(iid, info)
        setText()
    }

    fun setText() {
        setText(description.valueAsString)
    }
}
