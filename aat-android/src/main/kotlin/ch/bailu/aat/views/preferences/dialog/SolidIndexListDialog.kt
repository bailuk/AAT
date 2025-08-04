package ch.bailu.aat.views.preferences.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat_lib.preferences.SolidIndexList

class SolidIndexListDialog(context: Context, private val slist: SolidIndexList) : AbsSolidDialog(),
    DialogInterface.OnClickListener {
    val dialog: AlertDialog.Builder = createDefaultDialog(context, slist)

    init {
        dialog.setSingleChoiceItems(slist.getStringArray(), slist.index, this)
        dialog.create()
        dialog.show()
    }

    override fun onClick(dialog: DialogInterface, index: Int) {
        slist.index = index
        dialog.dismiss()
    }
}
