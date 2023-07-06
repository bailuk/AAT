package ch.bailu.aat.util.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.widget.EditText
import ch.bailu.aat.R

abstract class AppDialog {
    internal inner class PositiveClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            onPositiveClick()
        }
    }

    internal inner class NegativeClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            onNegativeClick()
        }
    }

    internal inner class NeutralClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            onNeutralClick()
        }
    }

    protected fun onNegativeClick() {}
    protected abstract fun onPositiveClick()
    open fun onNeutralClick() {}
    fun displayYesNoDialog(activity: Activity, title: String, text: String) {
        val builder = AlertDialog.Builder(activity)
        val dialog: Dialog
        builder.setTitle(title)
        builder.setMessage(text)
        builder.setCancelable(true)
        builder.setPositiveButton(activity.getString(R.string.dialog_yes), PositiveClickListener())
        builder.setNegativeButton(activity.getString(R.string.dialog_no), NegativeClickListener())
        dialog = builder.create()
        dialog.setOwnerActivity(activity)
        dialog.show()
    }

    fun displayTextDialog(activity: Activity, title: String, edit: EditText) {
        val builder = AlertDialog.Builder(activity)
        val dialog: Dialog
        builder.setTitle(title)
        builder.setView(edit)
        builder.setCancelable(true)
        builder.setPositiveButton(
            activity.getString(R.string.dialog_ok),
            PositiveClickListener()
        )
        builder.setNegativeButton(
            activity.getString(R.string.dialog_cancel),
            NegativeClickListener()
        )
        dialog = builder.create()
        dialog.setOwnerActivity(activity)
        dialog.show()
    }

    fun displaySaveDiscardDialog(activity: Activity, title: String) {
        val builder = AlertDialog.Builder(activity)
        val dialog: Dialog
        builder.setTitle(title)
        builder.setMessage(activity.getString(R.string.dialog_modified))
        builder.setCancelable(true)
        builder.setPositiveButton(activity.getString(R.string.dialog_save), PositiveClickListener())
        builder.setNeutralButton(
            activity.getString(R.string.dialog_discard),
            NeutralClickListener()
        )
        builder.setNegativeButton(
            activity.getString(R.string.dialog_cancel),
            NegativeClickListener()
        )
        dialog = builder.create()
        dialog.setOwnerActivity(activity)
        dialog.show()
    }
}
