package ch.bailu.aat.views.description.mview

import android.content.Context
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import ch.bailu.aat.preferences.Storage
import org.mapsforge.map.model.common.ObservableInterface
import org.mapsforge.map.model.common.Observer

class MultiView(context: Context, private val solidKey: String) : ViewGroup(context),
    ObservableInterface {
    private val observers = ArrayList<Observer>(2)
    override fun addObserver(o: Observer) {
        observers.remove(o)
        observers.add(o)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    val label: String
        get() = if (pages.size > 0) pages[active].label else ""

    inner class Page(val view: View, val label: String) {
        init {
            view.visibility = GONE
            addView(view)
        }
    }

    private val pages = ArrayList<Page>(5)
    private var active = 0
    fun inflateMenu(menu: Menu) {
        for (i in pages.indices) {
            menu.add(Menu.NONE, i, Menu.NONE, pages[i].label)
        }
    }

    fun add(view: View, label: String) {
        pages.add(Page(view, label))
    }

    fun add(view: View) {
        pages.add(Page(view, ""))
    }

    fun remove(i: Int) {
        if (i < pages.size && i > -1) {
            pages.removeAt(i)
        }
    }

    fun remove(view: View) {
        for (i in pages.indices.reversed()) {
            if (pages[i].view === view) {
                pages.removeAt(i)
            }
        }
    }

    fun pageCount(): Int {
        return pages.size
    }

    fun setNext() {
        setActive(active + 1)
    }

    fun setPrevious() {
        setActive(active - 1)
    }

    fun setActive(a: Int) {
        if (a != active) {
            pages[active].view.visibility = GONE
        }
        active = a
        if (active >= pages.size) active = 0 else if (active < 0) active = pages.size - 1
        pages[active].view.visibility = VISIBLE
        pages[active].view.bringToFront()
        for (observer in observers) {
            observer.onChange()
        }
    }

    override fun onMeasure(wSpec: Int, hSpec: Int) {
        onMeasureBiggestWins(wSpec, hSpec)
    }

    private fun onMeasureBiggestWins(wSpec: Int, hSpec: Int) {
        var width = MeasureSpec.getSize(wSpec)
        var height = MeasureSpec.getSize(hSpec)
        var pWidth = 0
        var pHeight = 0
        for (p in pages) {
            p.view.measure(wSpec, hSpec)
            pWidth = Math.max(pWidth, p.view.measuredWidth)
            pHeight = Math.max(pHeight, p.view.measuredHeight)
        }
        width = Math.min(width, pWidth)
        height = Math.min(height, pHeight)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (p in pages) {
            p.view.layout(0, 0, r - l, b - t)
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setActive(Storage(context).readInteger(solidKey + "_index"))
    }

    public override fun onDetachedFromWindow() {
        storeActive(context, solidKey, active)
        super.onDetachedFromWindow()
    }

    fun getActive(): Int {
        return active
    }

    companion object {
        fun storeActive(context: Context, key: String, active: Int) {
            Storage(context).writeInteger(key + "_index", active)
        }
    }

}
