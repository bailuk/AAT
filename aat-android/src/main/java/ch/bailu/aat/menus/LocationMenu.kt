package ch.bailu.aat.menus

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.location.SolidGoToLocation
import ch.bailu.aat.util.Clipboard
import ch.bailu.aat_lib.coordinates.OlcCoordinates
import ch.bailu.aat_lib.coordinates.WGS84Coordinates
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import org.mapsforge.core.model.LatLong

class LocationMenu(private val context: Context, private val map: MapViewInterface) : AbsMenu() {
    private val clipboard: Clipboard = Clipboard(context)
    private var paste: MenuItem? = null

    override fun inflate(menu: Menu) {
        add(menu, R.string.location_send) { send() }
        add(menu, R.string.location_view) { view() }
        add(menu, R.string.clipboard_copy) { copy() }

        paste = add(menu, R.string.clipboard_paste) { paste() }

        add(menu, SolidGoToLocation(context).label) { SolidGoToLocation(context).goToLocationFromUser(map) }
    }

    override val title: String
        get() = context.getString(R.string.location_title)

    override fun prepare(menu: Menu) {
        paste?.isEnabled = clipboard.text != null
    }

    private fun paste() {
        val s = clipboard.text.toString()
        SolidGoToLocation(context).goToLocation(map, s)
    }

    private fun copy() {
        val sgrid = SolidMapGrid(
            Storage(context),
            map.mContext.solidKey
        )
        clipboard.setText(sgrid.clipboardLabel, sgrid.getCode(center))
    }

    private fun view() {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse(WGS84Coordinates.getGeoUri(center))
        intent.data = uri
        context.startActivity(Intent.createChooser(intent, uri.toString()))
    }

    private fun send() {
        val intent = Intent(Intent.ACTION_SEND)
        val url = WGS84Coordinates.getGeoUri(center)
        val desc = WGS84Coordinates.getGeoPointDescription(center)
        val body = """
            $desc
            
            $url
            
            ${OlcCoordinates(center)}
            """.trimIndent()
        intent.type = "label/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, url)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(intent, url))
    }

    private val center: LatLong
        get() = map.mapViewPosition.center
}
