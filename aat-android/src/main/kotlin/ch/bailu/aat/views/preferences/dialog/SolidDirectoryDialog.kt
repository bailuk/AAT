package ch.bailu.aat.views.preferences.dialog

import android.app.Activity
import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.preferences.SolidSAF
import ch.bailu.aat_lib.preferences.SolidFile

class SolidDirectoryDialog(private val acontext: Activity, sdirectory: SolidFile) :
    SolidStringDialog(acontext, sdirectory) {
    private val saf: SolidSAF = SolidSAF(sdirectory)

    override fun buildExtraSelection(context: Context, sel: ArrayList<String>) {
        super.buildExtraSelection(context, sel)
        sel.add(context.getString(R.string.pick))
    }

    override fun onExtraItemClick(index: Int) {
        if (index == 1) saf.setFromPickerActivity(acontext) else super.onExtraItemClick(index)
    }
}
