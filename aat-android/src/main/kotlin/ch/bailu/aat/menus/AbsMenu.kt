package ch.bailu.aat.menus

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

abstract class AbsMenu {
    abstract val title: String

    abstract fun inflate(menu: Menu)
    abstract fun prepare(menu: Menu)

    fun showAsPopup(context: Context, view: View) {
        PopupMenu(context, view).apply {
            inflate(menu)
            prepare(menu)
            setOnMenuItemClickListener { item: MenuItem -> onItemClick(item) }
            show()
        }
    }

    fun showAsDialog(context: Context) {
        MenuDialog(context, this)
    }

    private class Item(private val item: MenuItem, private val onClick: ()->Unit) {
        fun onClick(item: MenuItem): Boolean {
            val result = this.item === item
            if (result) {
                onClick()
            }
            return result
        }
    }

    private val items = ArrayList<Item>(5)

    fun add(menu: Menu, name: String, onClick: ()->Unit): MenuItem {
        val result = menu.add(name)
        items.add(Item(result, onClick))
        return result
    }

    fun add(menu: Menu, nameID: Int, onClick: ()->Unit): MenuItem {
        val result = menu.add(nameID)
        items.add(Item(result, onClick))
        return result
    }

    open fun onItemClick(item: MenuItem): Boolean {
        for (i in items) {
            if (i.onClick(item)) return true
        }
        return false
    }
}
