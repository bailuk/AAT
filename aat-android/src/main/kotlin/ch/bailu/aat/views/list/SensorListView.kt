package ch.bailu.aat.views.list

import android.widget.LinearLayout
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.SensorService
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID

class SensorListView(sc: ServiceContext, theme: UiTheme) : LinearLayout(sc.getContext()),
    OnContentUpdatedInterface {
    private val scontext: ServiceContext
    private val children = ArrayList<SensorListItemView>(10)
    private val theme: UiTheme

    init {
        orientation = VERTICAL
        scontext = sc
        this.theme = theme
        updateViews()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (iid == InfoID.SENSORS) {
            updateViews()
        }
    }

    private fun updateViews() {
        scontext.insideContext {
            val sensorList = (scontext.getSensorService() as SensorService).sensorList
            for (i in 0 until sensorList.size()) {
                if (children.size <= i) {
                    children.add(SensorListItemView(scontext, sensorList.get(i), theme))
                    addView(children[i])
                } else {
                    children[i].setItem(sensorList.get(i))
                }
            }
            for (i in children.size - 1 downTo sensorList.size()) {
                removeView(children[i])
                children.removeAt(i)
            }
        }
    }
}
