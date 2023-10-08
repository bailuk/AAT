package ch.bailu.aat.views.preferences.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import ch.bailu.aat_lib.preferences.SolidCheckList

class SolidCheckListDialog(context: Context?, private val slist: SolidCheckList) : AbsSolidDialog(),
    OnMultiChoiceClickListener {
    init {
        val dialog = createDefaultDialog(context, slist)
        dialog.setMultiChoiceItems(slist.getStringArray(), slist.getEnabledArray(), this)
        dialog.create()
        dialog.show()
    }

    override fun onClick(dialog: DialogInterface, i: Int, isChecked: Boolean) {
        slist.setEnabled(i, isChecked)
    }
}
