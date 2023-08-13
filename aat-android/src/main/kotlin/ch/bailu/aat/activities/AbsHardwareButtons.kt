package ch.bailu.aat.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.preferences.SolidVolumeKeys

abstract class AbsHardwareButtons : AbsBackButton() {
    private var svolumeKeys: SolidVolumeKeys? = null

    enum class EventType {
        UP, DOWN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        svolumeKeys = SolidVolumeKeys(Storage(this))
    }

    override fun onKeyDown(code: Int, event: KeyEvent): Boolean {
        return onKey(code, EventType.DOWN) || super.onKeyDown(code, event)
    }

    override fun onKeyUp(code: Int, event: KeyEvent): Boolean {
        return onKey(code, EventType.UP) || super.onKeyUp(code, event)
    }

    private fun onKey(code: Int, type: EventType): Boolean {
        if (code == KeyEvent.KEYCODE_VOLUME_UP || code == KeyEvent.KEYCODE_VOLUME_DOWN) {
            val svolumeKeys = this.svolumeKeys
            if (svolumeKeys is SolidVolumeKeys && svolumeKeys.isEnabled) {
                if (onHardwareButtonPressed(code, type)) return true
            }
        } else if (code == KeyEvent.KEYCODE_SEARCH) { // in app search
            if (onHardwareButtonPressed(code, type)) return true
        }
        return false
    }

    interface OnHardwareButtonPressed {
        fun onHardwareButtonPressed(code: Int, type: EventType): Boolean
    }

    private fun onHardwareButtonPressed(code: Int, type: EventType): Boolean {
        return onHardwareButtonPressed(window.decorView, code, type)
    }

    private fun onHardwareButtonPressed(view: View, code: Int, type: EventType): Boolean {
        if (view is OnHardwareButtonPressed) {
            if (view.onHardwareButtonPressed(code, type)) return true
        }
        return if (view is ViewGroup) {
            onHardwareButtonPressedChildren(view, code, type)
        } else false
    }

    private fun onHardwareButtonPressedChildren(
        parent: ViewGroup,
        code: Int,
        type: EventType
    ): Boolean {
        val count = parent.childCount
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            if (onHardwareButtonPressed(view, code, type)) return true
        }
        return false
    }
}
