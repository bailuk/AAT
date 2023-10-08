package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.view.description.NumberView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.description.AltitudeDescription
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.PredictiveTimeDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.gtk.FlowBox
import ch.bailu.gtk.gtk.ScrolledWindow

class CockpitView(private val appContext: AppContext) {

    private val flow = FlowBox().apply {
        vexpand = true
        hexpand = true
        homogeneous = false
    }
    val scrolledWindow = ScrolledWindow().apply {
        child = flow
    }

    private val descriptions = ArrayList<ContentDescription>()
    private val views = ArrayList<NumberView>()

    fun add(di: DispatcherInterface, cd: ContentDescription, vararg iid: Int) : NumberView {
        val v = NumberView(cd)

        flow.insert(v.box, -1)
        di.addTarget(v, *iid)
        descriptions.add(cd)
        views.add(v)

       return v
    }

    fun addDefaults(dispatcher: DispatcherInterface) {
        add(dispatcher, CurrentSpeedDescription(appContext.storage), InfoID.LOCATION)
        add(dispatcher, AltitudeDescription(appContext.storage), InfoID.LOCATION)

        add(dispatcher, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
        add(dispatcher, DistanceDescription(appContext.storage), InfoID.TRACKER)
        add(dispatcher, AverageSpeedDescriptionAP(appContext.storage), InfoID.TRACKER)
        add(dispatcher, MaximumSpeedDescription(appContext.storage), InfoID.TRACKER)
    }
}
