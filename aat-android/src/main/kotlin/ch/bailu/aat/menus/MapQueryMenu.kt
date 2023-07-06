package ch.bailu.aat.menus

import android.content.Context
import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.activities.NominatimActivity
import ch.bailu.aat.activities.OverpassActivity
import ch.bailu.aat.activities.PoiActivity
import ch.bailu.aat.app.ActivitySwitcher.Companion.start
import ch.bailu.aat.util.NominatimApi
import ch.bailu.aat.util.OverpassApi.Companion.getName
import ch.bailu.aat_lib.map.MapContext

class MapQueryMenu(private val context: Context, private val mcontext: MapContext) : AbsMenu() {
    override fun prepare(menu: Menu) {}
    override fun inflate(menu: Menu) {
        add(menu, NominatimApi.API_NAME) {
            start(
                context,
                NominatimActivity::class.java,
                mcontext.metrics.boundingBox
            )
        }
        add(menu, getName(context)) {
            start(
                context,
                OverpassActivity::class.java,
                mcontext.metrics.boundingBox
            )
        }
        add(menu, R.string.p_mapsforge_poi) {
            start(
                context,
                PoiActivity::class.java,
                mcontext.metrics.boundingBox
            )
        }
    }

    override val title: String
        get() = context.getString(R.string.intro_nominatim)
}
