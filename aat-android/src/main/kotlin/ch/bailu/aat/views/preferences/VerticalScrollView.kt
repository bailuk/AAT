package ch.bailu.aat.views.preferences

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.theme.AppTheme.padding
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.description.DescriptionLabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.foc_android.FocAndroidFactory

open class VerticalScrollView(context: Context) : ScrollView(context) {
    private val layout: LinearLayout

    init {
        layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        addView(layout)
    }

    fun add(view: View) {
        padding(view)
        layout.addView(view)
    }

    fun add(di: DispatcherInterface, d: ContentDescription, theme: UiTheme, vararg iid: Int) {
        val v = DescriptionLabelTextView(context, d, theme)
        add(v)
        for (i in iid) di.addTarget(v, i)
    }

    fun addAllContent(
        di: DispatcherInterface,
        descriptions: Array<ContentDescription>,
        theme: UiTheme,
        vararg iid: Int
    ) {
        for (description in descriptions) {
            add(di, description, theme, *iid)
        }
    }

    fun addAllFilterViews(mc: MapContext, theme: UiTheme) {
        val sdirectory = SolidDirectoryQuery(Storage(context), FocAndroidFactory(context))

        val geo = LinearLayout(context)
        geo.addView(SolidCheckBox(context, sdirectory.useGeo, theme))
        geo.addView(SolidBoundingBoxView(context, sdirectory.boundingBox, mc, theme))
        layout.addView(geo)

        val from = LinearLayout(context)
        from.addView(SolidCheckBox(context, sdirectory.useDateStart, theme))
        from.addView(SolidDateView(context, sdirectory.dateStart, theme))
        layout.addView(from)

        val to = LinearLayout(context)
        to.addView(SolidCheckBox(context, sdirectory.useDateEnd, theme))
        to.addView(SolidDateView(context, sdirectory.dateTo, theme))
        layout.addView(to)
    }
}
