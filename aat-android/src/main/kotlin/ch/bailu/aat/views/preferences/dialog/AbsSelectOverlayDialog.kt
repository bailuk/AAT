package ch.bailu.aat.views.preferences.dialog

import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlayList
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

abstract class AbsSelectOverlayDialog(context: Context, private val slist: SolidCustomOverlayList = SolidCustomOverlayList(Storage(context), FocAndroidFactory(context))): AbsSolidDialog(), DialogInterface.OnClickListener {
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

    protected abstract fun onFileSelected(slist: SolidCustomOverlayList, index: Int, file: Foc)
}
