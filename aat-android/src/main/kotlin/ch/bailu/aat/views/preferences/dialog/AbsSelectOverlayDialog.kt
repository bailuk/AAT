package ch.bailu.aat.views.preferences.dialog

import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayList
import ch.bailu.foc.Foc

abstract class AbsSelectOverlayDialog(appContext: AppContext, context: Context, private val slist: SolidOverlayList<SolidCustomOverlay> = SolidOverlayList.createCustomOverlayList(appContext)
): AbsSolidDialog(), DialogInterface.OnClickListener {
    init {

        val dialog = createDefaultDialog(context, slist)
        dialog.setItems(slist.getStringArray(), this)
        dialog.create()
        dialog.show()
    }

    override fun onClick(dialog: DialogInterface, index: Int) {
        val file = slist[index].getValueAsFile()
        onFileSelected(slist, index, file)
        dialog.dismiss()
    }

    protected abstract fun onFileSelected(slist: SolidOverlayList<SolidCustomOverlay>, index: Int, file: Foc)
}
