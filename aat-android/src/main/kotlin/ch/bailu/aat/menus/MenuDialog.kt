package ch.bailu.aat.menus

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class MenuDialog(context: Context, private val menu: AbsMenu) {

    init {
        val title = menu.title
        val array = MenuArray(context)
        menu.inflate(array)
        menu.prepare(array)

        AlertDialog.Builder(context).apply {
            if (title.isNotEmpty()) {
                setTitle(title)
            }

            setItems(array.toStringArray()) { dialog: DialogInterface, index: Int ->
                menu.onItemClick(array.getItem(index))
                dialog.dismiss()
            }
            create()
            show()
        }

    }
}
