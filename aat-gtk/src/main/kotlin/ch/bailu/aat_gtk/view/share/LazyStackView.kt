package ch.bailu.aat_gtk.view.share

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

class LazyStackView(val stack: Stack, private val key: String, private val storage: StorageInterface) {
    private val pages = ArrayList<LazyPage>()

    val widget: Widget
        get() = stack

    fun add(title: String, initializer: () -> Widget): LazyPage {
        val result = LazyPage(title, initializer)
        pages.add(result)
        return result
    }

    fun show(index: Int): Int {
        if (pages.size > index && index > -1) {
            pages[index].show()
            storage.writeInteger(key, index)
            return index
        }
        return -1
    }

    fun restore(): Int {
        return show(storage.readInteger(key))
    }

    inner class LazyPage(val title: String, initializer: ()-> Widget) {
        private val widget by lazy {
            val result = initializer()
            stack.addChild(result)
            result
        }

        fun show() {
            stack.visibleChild = widget
        }
    }

    fun createCombo(): ComboBoxText {
        val result = ComboBoxText()

        pages.forEach {
            result.insertText(-1, Str(it.title))
        }
        return result
    }


    fun lastIndex(): Int {
        return pages.lastIndex
    }
}

