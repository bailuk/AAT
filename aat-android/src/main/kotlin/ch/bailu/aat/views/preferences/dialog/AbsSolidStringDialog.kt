package ch.bailu.aat.views.preferences.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidString

abstract class AbsSolidStringDialog(context: Context, private val solid: SolidString) : AbsSolidDialog(),
    DialogInterface.OnClickListener {
    private val selection: Array<String>
    private val baseSelectionSize: Int

    init {
        val selectionList = solid.buildSelection()
        baseSelectionSize = selectionList.size
        buildExtraSelection(context, selectionList)
        selection = selectionList.toTypedArray()
        val dialog: AlertDialog.Builder = createDefaultDialog(context, solid)
        dialog.setItems(selection, this)
        dialog.create()
        dialog.show()
    }

    protected abstract fun buildExtraSelection(context: Context, sel: ArrayList<String>)
    protected abstract fun onExtraItemClick(index: Int)
    override fun onClick(dialog: DialogInterface, index: Int) {
        if (index < baseSelectionSize) {
            try {
                solid.setValueFromString(selection[index])
            } catch (e: ValidationException) {
                AppLog.e(this, e)
            }
        } else {
            onExtraItemClick(index - baseSelectionSize)
        }
        dialog.dismiss()
    }
}
