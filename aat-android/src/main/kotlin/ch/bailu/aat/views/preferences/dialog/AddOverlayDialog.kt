package ch.bailu.aat.views.preferences.dialog

import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

class AddOverlayDialog(
    context: Context,
    private val slist: SolidCustomOverlayList,
    private val file: Foc
) : AbsSolidDialog(), DialogInterface.OnClickListener {
    init {
        val dialog = createDefaultDialog(context, slist)
        dialog.setItems(slist.stringArray, this)
        dialog.create()
        dialog.show()
    }

    constructor(context: Context, file: Foc) : this(
        context, SolidCustomOverlayList(
            Storage(context), FocAndroidFactory(context)
        ), file
    ) {
    }

    override fun onClick(dialog: DialogInterface, index: Int) {
        slist[index].setValueFromFile(file)
        dialog.dismiss()
    }
}
