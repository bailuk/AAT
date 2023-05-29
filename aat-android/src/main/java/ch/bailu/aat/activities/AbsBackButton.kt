package ch.bailu.aat.activities

import android.content.Context
import android.view.View
import android.view.ViewGroup

abstract class AbsBackButton : AbsActivity() {
    override fun onBackPressed() {
        if (!onBackPressed(window.decorView)) super.onBackPressed()
    }

    fun onBackPressedMenuBar() {
        super.onBackPressed()
    }

    abstract class OnBackPressedListener(context: Context) : View(context){
        init {
            visibility = INVISIBLE
        }

        abstract fun onBackPressed(): Boolean
    }

    private fun onBackPressed(view: View): Boolean {
        if (view is OnBackPressedListener) {
            if (view.onBackPressed()) return true
        }
        return if (view is ViewGroup) {
            onBackPressedChildren(view)
        } else false
    }

    private fun onBackPressedChildren(parent: ViewGroup): Boolean {
        val count = parent.childCount
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            if (onBackPressed(view)) return true
        }
        return false
    }
}
