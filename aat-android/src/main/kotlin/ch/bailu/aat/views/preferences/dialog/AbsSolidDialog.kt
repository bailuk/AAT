package ch.bailu.aat.views.preferences.dialog

import android.app.AlertDialog
import android.content.Context
import ch.bailu.aat.generated.Images
import ch.bailu.aat_lib.preferences.AbsSolidType

abstract class AbsSolidDialog {
    companion object {
        @JvmStatic
        fun createDefaultDialog(context: Context?, s: AbsSolidType): AlertDialog.Builder {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle(s.getLabel())
            if ("" != s.getIconResource()) {
                dialog.setIcon(Images.get(s.getIconResource()))
            }
            return dialog
        }
    }
}
