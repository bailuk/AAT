package ch.bailu.aat.views.preferences.dialog

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat_lib.preferences.SolidString

open class SolidStringDialog(private val context: Context, private val solid: SolidString) :
    AbsSolidStringDialog(context, solid) {
    override fun buildExtraSelection(context: Context, sel: ArrayList<String>) {
        sel.add(context.getString(R.string.enter))
    }

    override fun onExtraItemClick(index: Int) {
        SolidTextInputDialog(context, solid, SolidTextInputDialog.TEXT)
    }
}
