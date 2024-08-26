package ch.bailu.aat.util.ui

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.presets.SolidBacklight
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import java.io.Closeable


class Backlight(private val activity: Activity, private val scontext: ServiceContext) :
    TargetInterface, OnPreferencesChanged, Closeable {
    private val window: Window = activity.window
    private val spreset: SolidPreset = SolidPreset(Storage(scontext.getContext()))
    private var sbacklight: SolidBacklight
    private var state = StateID.OFF

    init {
        sbacklight = setToPreset()
        spreset.register(this)
    }

    fun setBacklightAndPreset() {
        setToPreset()
        setBacklight()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (state != info.getState()) {
            state = info.getState()
            setBacklight()
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (spreset.hasKey(key)) {
            setBacklightAndPreset()
        } else if (sbacklight.hasKey(key)) {
            setBacklight()
        }
    }

    private fun setToPreset(): SolidBacklight {
        val presetIndex = presetIndex
        sbacklight = SolidBacklight(scontext.getContext(), presetIndex)
        return sbacklight
    }

    private val presetIndex: Int
        get() {
            val result = IntArray(1)
            scontext.insideContext { result[0] = scontext.getTrackerService().getPresetIndex() }
            return result[0]
        }

    private fun setBacklight() {
        if (state == StateID.ON && sbacklight.keepScreenOn()) {
            keepOn()
        } else {
            autoOff()
        }
    }

    private fun autoOff() {
        keepScreenOn(false)
        dismissKeyGuard(false)
    }

    private fun keepOn() {
        keepScreenOn(true)
        dismissKeyGuard(sbacklight.dismissKeyGuard())
    }

    private fun dismissKeyGuard(dismiss: Boolean) {
        if (Build.VERSION.SDK_INT >= 27) {
            dismissKeyGuardSDK27(dismiss)
        } else if (Build.VERSION.SDK_INT >= 26) {
            dismissKeyGuardSDK26(dismiss)
        } else {
            dismissKeyGuardSDK5(dismiss)
        }
    }

    @RequiresApi(api = 27)
    private fun dismissKeyGuardSDK27(dismiss: Boolean) {
        activity.setShowWhenLocked(dismiss)
    }

    @RequiresApi(api = 26)
    @Suppress("DEPRECATION")
    private fun dismissKeyGuardSDK26(dismiss: Boolean) {
        updateFlags(dismiss, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
    }

    @Suppress("DEPRECATION")
    private fun dismissKeyGuardSDK5(dismiss: Boolean) {
        updateFlags(
            dismiss, WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )
    }

    private fun keepScreenOn(keepOn: Boolean) {
        updateFlags(keepOn, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun updateFlags(on: Boolean, flags: Int) {
        if (on) {
            window.addFlags(flags)
        } else {
            window.clearFlags(flags)
        }
    }

    override fun close() {
        spreset.unregister(this)
    }
}
