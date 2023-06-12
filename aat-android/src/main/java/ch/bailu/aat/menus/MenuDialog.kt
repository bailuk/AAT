package ch.bailu.aat.menus

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class MenuDialog(context: Context, private val menu: AbsMenu) : DialogInterface.OnClickListener {

    private val array: MenuArray

    init {
        val title = menu.title
        array = MenuArray(context)
        menu.inflate(array)
        menu.prepare(array)
        val dialog = AlertDialog.Builder(context)
        if (title.isNotEmpty()) dialog.setTitle(title)
        dialog.setItems(array.toStringArray(), this)
        dialog.create()
        dialog.show()
    }

    override fun onClick(dialog: DialogInterface, i: Int) {
        menu.onItemClick(array.getItem(i))
        dialog.dismiss()
    }
}
