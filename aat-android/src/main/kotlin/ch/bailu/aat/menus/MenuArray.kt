package ch.bailu.aat.menus

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.ActionProvider
import android.view.ContextMenu.ContextMenuInfo
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.SubMenu
import android.view.View

class MenuArray(private val context: Context) : Menu {
    private val item = ArrayList<Item>()
    override fun add(charSequence: CharSequence): MenuItem {
        val i = Item(context, charSequence)
        item.add(i)
        return i
    }

    override fun add(i0: Int, id: Int, i2: Int, charSequence: CharSequence): MenuItem {
        val i = Item(context, charSequence, id)
        item.add(i)
        return i
    }

    override fun size(): Int {
        return item.size
    }

    override fun getItem(i: Int): MenuItem {
        return item[i]
    }

    fun toStringArray(): Array<CharSequence?> {
        val r = arrayOfNulls<CharSequence>(item.size)
        for (i in item.indices) {
            r[i] = item[i].title
        }
        return r
    }

    override fun add(i: Int): MenuItem {
        return add(context.getString(i))
    }

    override fun add(i: Int, id: Int, i2: Int, res: Int): MenuItem {
        return add(i, id, i2, context.getString(res))
    }

    override fun addSubMenu(charSequence: CharSequence): SubMenu? {
        return null
    }

    override fun addSubMenu(i: Int): SubMenu? {
        return null
    }

    override fun addSubMenu(i: Int, i1: Int, i2: Int, charSequence: CharSequence): SubMenu? {
        return null
    }

    override fun addSubMenu(i: Int, i1: Int, i2: Int, i3: Int): SubMenu? {
        return null
    }

    override fun addIntentOptions(
        i: Int,
        i1: Int,
        i2: Int,
        componentName: ComponentName,
        intents: Array<Intent>,
        intent: Intent,
        i3: Int,
        menuItems: Array<MenuItem>
    ): Int {
        return 0
    }

    override fun removeItem(i: Int) {}
    override fun removeGroup(i: Int) {}
    override fun clear() {}
    override fun setGroupCheckable(i: Int, b: Boolean, b1: Boolean) {}
    override fun setGroupVisible(i: Int, b: Boolean) {}
    override fun setGroupEnabled(i: Int, b: Boolean) {}
    override fun hasVisibleItems(): Boolean {
        return false
    }

    override fun findItem(i: Int): MenuItem? {
        return null
    }

    override fun close() {}
    override fun performShortcut(i: Int, keyEvent: KeyEvent, i1: Int): Boolean {
        return false
    }

    override fun isShortcutKey(i: Int, keyEvent: KeyEvent): Boolean {
        return false
    }

    override fun performIdentifierAction(i: Int, i1: Int): Boolean {
        return false
    }

    override fun setQwertyMode(b: Boolean) {}
    private class Item @JvmOverloads constructor(
        private val context: Context,
        private var title: CharSequence?,
        private val id: Int = 0
    ) : MenuItem {
        override fun setTitle(t: CharSequence?): MenuItem {
            title = t
            return this
        }

        override fun setTitle(i: Int): MenuItem {
            return setTitle(context.getString(i))
        }

        override fun getTitle(): CharSequence? {
            return title
        }

        override fun getItemId(): Int {
            return id
        }

        override fun getGroupId(): Int {
            return 0
        }

        override fun getOrder(): Int {
            return 0
        }

        override fun setTitleCondensed(charSequence: CharSequence?): MenuItem {
            return this
        }

        override fun getTitleCondensed(): CharSequence? {
            return null
        }

        override fun setIcon(drawable: Drawable?): MenuItem {
            return this
        }

        override fun setIcon(i: Int): MenuItem {
            return this
        }

        override fun getIcon(): Drawable? {
            return null
        }

        override fun setIntent(intent: Intent?): MenuItem {
            return this
        }

        override fun getIntent(): Intent? {
            return null
        }

        override fun setShortcut(c: Char, c1: Char): MenuItem {
            return this
        }

        override fun setNumericShortcut(c: Char): MenuItem {
            return this
        }

        override fun getNumericShortcut(): Char {
            return Char(0)
        }

        override fun setAlphabeticShortcut(c: Char): MenuItem {
            return this
        }

        override fun getAlphabeticShortcut(): Char {
            return Char(0)
        }

        override fun setCheckable(b: Boolean): MenuItem {
            return this
        }

        override fun isCheckable(): Boolean {
            return false
        }

        override fun setChecked(b: Boolean): MenuItem {
            return this
        }

        override fun isChecked(): Boolean {
            return false
        }

        override fun setVisible(b: Boolean): MenuItem {
            return this
        }

        override fun isVisible(): Boolean {
            return false
        }

        override fun setEnabled(b: Boolean): MenuItem {
            return this
        }

        override fun isEnabled(): Boolean {
            return false
        }

        override fun hasSubMenu(): Boolean {
            return false
        }

        override fun getSubMenu(): SubMenu? {
            return null
        }

        override fun setOnMenuItemClickListener(onMenuItemClickListener: MenuItem.OnMenuItemClickListener?): MenuItem {
            return this
        }

        override fun getMenuInfo(): ContextMenuInfo? {
            return null
        }

        override fun setShowAsAction(i: Int) {}
        override fun setShowAsActionFlags(i: Int): MenuItem {
            return this
        }

        override fun setActionView(view: View?): MenuItem {
            return this
        }

        override fun setActionView(i: Int): MenuItem {
            return this
        }

        override fun getActionView(): View? {
            return null
        }

        override fun setActionProvider(actionProvider: ActionProvider?): MenuItem {
            return this
        }

        override fun getActionProvider(): ActionProvider? {
            return null
        }

        override fun expandActionView(): Boolean {
            return false
        }

        override fun collapseActionView(): Boolean {
            return false
        }

        override fun isActionViewExpanded(): Boolean {
            return false
        }

        override fun setOnActionExpandListener(onActionExpandListener: OnActionExpandListener?): MenuItem {
            return this
        }
    }
}
