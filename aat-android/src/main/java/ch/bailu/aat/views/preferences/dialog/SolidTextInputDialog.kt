package ch.bailu.aat.views.preferences.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.View
import android.widget.EditText
import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.resources.Res

class SolidTextInputDialog(context: Context, solid: AbsSolidType, inputType: Int, onClick: View.OnClickListener) : AbsSolidDialog() {
    init {
        val builder = createDefaultDialog(context, solid)
        builder.setPositiveButton(Res.str().ok()) { _: DialogInterface?, _: Int -> }
        builder.setNegativeButton(Res.str().cancel()) { _: DialogInterface, _: Int -> }

        val dialog = builder.create()
        val input = EditText(context)
        input.inputType = inputType
        dialog.setView(input)
        input.setText(solid.valueAsString)
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { view: View? ->
            try {
                solid.setValueFromString(input.text.toString())
                onClick.onClick(view)
                dialog.dismiss()
            } catch (e: ValidationException) {
                input.error = e.message
            }
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setOnClickListener { dialog.dismiss() }
    }

    constructor(context: Context, solid: AbsSolidType, inputType: Int) : this(context, solid, inputType, { })

    companion object {
        const val INTEGER = InputType.TYPE_CLASS_NUMBER
        const val INTEGER_SIGNED = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        const val FLOAT = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        const val TEXT = InputType.TYPE_CLASS_TEXT
    }
}
