package ch.bailu.aat.views.list

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.menus.FileMenu
import ch.bailu.aat.util.ui.AppLayout.getBigButtonSize
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.image.PreviewView
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.foc_android.FocAndroid

class GpxListEntryView(
    acontext: ActivityContext,
    private val descriptions: Array<ContentDescription>,
    theme: UiTheme?
) : LinearLayout(acontext), OnContentUpdatedInterface {
    private val preview: PreviewView
    private val labelTextView: LabelTextView
    private var file = FocAndroid.NULL
    private fun addViewWeight(v: View) {
        addView(v)
        val l = v.layoutParams as LayoutParams
        l.weight = 1f
        v.layoutParams = l
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        file = info.file
        for (description in descriptions) {
            description.onContentUpdated(iid, info)
        }
        updateText()
        preview.onContentUpdated(iid, info)
    }

    fun themify(theme: UiTheme) {
        labelTextView.themify(theme)
        theme.button(labelTextView)
    }

    private val builder = StringBuilder(20)

    init {
        orientation = HORIZONTAL
        val p = AbsListView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = p
        labelTextView = LabelTextView(context, "", theme!!)
        addViewWeight(labelTextView)
        val previewSize = getBigButtonSize(acontext)
        preview = PreviewView(acontext.serviceContext, acontext.appContext.summaryConfig)
        addView(preview, previewSize, previewSize)
        preview.setOnClickListener {
            FileMenu(
                acontext,
                file
            ).showAsDialog(acontext)
        }
    }

    private fun updateText() {
        builder.setLength(0)
        for (i in descriptions.indices) {
            if (i == 0) {
                labelTextView.setLabel(descriptions[i].getValueAsString())
            } else {
                if (i > 1) builder.append(" - ")
                builder.append(descriptions[i].getValueAsString())
            }
        }
        labelTextView.setText(builder)
    }
}
