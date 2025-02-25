package ch.bailu.aat.views.description

import android.content.Context
import android.view.ViewGroup
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.location.AndroidSolidAltitudeUtil.requestOnClick
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.description.AltitudeConfigurationDescription
import ch.bailu.aat_lib.description.CadenceDescription
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.HeartRateDescription
import ch.bailu.aat_lib.description.PowerDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.view.cockpit.Layouter

class CockpitView(context: Context, private val theme: UiTheme) : ViewGroup(context) {
    private val layouter: Layouter
    private val storage: StorageInterface
    private val contentDescriptions = ArrayList<ContentDescription>()

    init {
        storage = Storage(context)
        theme.background(this)
        layouter = Layouter(
            contentDescriptions
        ) { index: Int, x: Int, y: Int, x2: Int, y2: Int -> getChildAt(index).layout(x, y, x2, y2) }
    }

    fun add(di: DispatcherInterface, de: ContentDescription): NumberView {
        return add(di, de, InfoID.TRACKER)
    }

    fun addC(di: DispatcherInterface, de: ContentDescription, vararg iid: Int): NumberView {
        return internalAddView(di, ColorNumberView(context, de, theme), *iid)
    }

    fun add(di: DispatcherInterface, de: ContentDescription, vararg iid: Int): NumberView {
        return internalAddView(di, NumberView(context, de, theme), *iid)
    }

    private fun internalAddView(di: DispatcherInterface, v: NumberView, vararg iid: Int): NumberView {
        addView(v)
        di.addTarget(v, *iid)
        contentDescriptions.add(v.description)
        return v
    }

    fun addAltitude(di: DispatcherInterface) {
        val v = add(di, AltitudeConfigurationDescription(storage), InfoID.LOCATION)
        requestOnClick(v)
    }

    fun addHeartRate(di: DispatcherInterface) {
        val v = add(di, HeartRateDescription(), InfoID.HEART_RATE_SENSOR)
        v.requestOnClickSensorReconnect()
    }

    fun addPower(di: DispatcherInterface) {
        val v = add(di, PowerDescription(), InfoID.POWER_SENSOR)
        v.requestOnClickSensorReconnect()
    }

    fun addCadence(di: DispatcherInterface) {
        val v = add(di, CadenceDescription(), InfoID.CADENCE_SENSOR)
        v.requestOnClickSensorReconnect()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            layouter.layout(r - l, b - t)
        }
    }
}
