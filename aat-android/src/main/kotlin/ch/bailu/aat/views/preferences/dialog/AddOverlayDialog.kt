package ch.bailu.aat.views.preferences.dialog

import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayList
import ch.bailu.foc.Foc

class AddOverlayDialog(
    context: Context,
    private val slist: SolidOverlayList<SolidCustomOverlay>,
    private val file: Foc
) : AbsSolidDialog(), DialogInterface.OnClickListener {
    init {
        val dialog = createDefaultDialog(context, slist)
        dialog.setItems(slist.getStringArray(), this)
        dialog.create()
        dialog.show()
    }

    constructor(appContext: AppContext, context: Context, file: Foc) : this (
        context, SolidOverlayList.createCustomOverlayList(appContext), file
    )

    override fun onClick(dialog: DialogInterface, index: Int) {
        slist[index].setValueFromFile(file)
        dialog.dismiss()
    }
}
