package ch.bailu.aat.menus

import android.content.Context
import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.activities.PreferencesActivity
import ch.bailu.aat.app.ActivitySwitcher.Companion.start
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.AndroidMapDirectories
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.preferences.dialog.SolidCheckListDialog
import ch.bailu.aat.views.preferences.dialog.SolidStringDialog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.foc.FocFactory
import ch.bailu.foc_android.FocAndroidFactory

class MapMenu(private val context: Context, private val mcontext: MapContext) : AbsMenu() {

    override fun inflate(menu: Menu) {
        val foc: FocFactory = FocAndroidFactory(context)
        val storage: StorageInterface = Storage(context)
        val sdir = SolidMapsForgeDirectory(storage, foc, AndroidMapDirectories(context))
        val stheme = SolidRenderTheme(sdir, foc)
        val smapFile = AndroidMapDirectories(context).createSolidFile()

        add(menu, R.string.p_map) {
            SolidCheckListDialog(
                context,
                SolidMapTileStack(stheme)
            )
        }
        add(menu, R.string.p_overlay) {
            SolidCheckListDialog(
                context,
                SolidCustomOverlayList(storage, foc)
            )
        }.apply {
            setIcon(R.drawable.view_paged_inverse)
        }
        add(menu, smapFile.label) {
            SolidStringDialog(
                context,
                AndroidMapDirectories(context).createSolidFile()
            )
        }
        add(menu,SolidRenderTheme(smapFile, FocAndroidFactory(context)).label) {
            SolidStringDialog(
                context,
                stheme
            )
        }
        add(menu, R.string.intro_settings) {
            MultiView.storeActive(context, PreferencesActivity.SOLID_KEY, 1)
            start(context, PreferencesActivity::class.java)
        }

        add(menu, R.string.tt_info_reload) {mcontext.mapView.reDownloadTiles()}.apply {
            setIcon(R.drawable.view_refresh)
        }
    }

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}
}
