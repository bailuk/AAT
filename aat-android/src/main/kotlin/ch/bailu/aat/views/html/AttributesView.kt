package ch.bailu.aat.views.html

import android.content.Context
import android.graphics.Color
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.preferences.StorageInterface

class AttributesView(context: Context, storage: StorageInterface) : HtmlTextView(context), TargetInterface {
    private val markupBuilder: MarkupBuilderGpx = MarkupBuilderGpx(storage)

    init {
        enableAutoLink()
        setPadding(10, 10, 10, 10)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setBackgroundColor(MapColor.toLightTransparent(MapColor.getColorFromIID(iid)))
        setTextColor(Color.BLACK)
        markupBuilder.appendAttributes(info.getAttributes())
        setHtmlText(markupBuilder.toString())
        markupBuilder.clear()
    }
}
