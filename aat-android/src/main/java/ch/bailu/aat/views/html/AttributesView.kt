package ch.bailu.aat.views.html

import android.content.Context
import android.graphics.Color
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.preferences.StorageInterface

class AttributesView(context: Context, storage: StorageInterface) : HtmlTextView(context), OnContentUpdatedInterface {
    private val markupBuilder: MarkupBuilderGpx

    init {
        markupBuilder = MarkupBuilderGpx(storage)
        enableAutoLink()
        setPadding(10, 10, 10, 10)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setBackgroundColor(MapColor.toLightTransparent(MapColor.getColorFromIID(iid)))
        setTextColor(Color.BLACK)
        markupBuilder.appendAttributes(info.attributes)
        setHtmlText(markupBuilder.toString())
        markupBuilder.clear()
    }
}
