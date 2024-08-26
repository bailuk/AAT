package ch.bailu.aat_lib.map.layer

import ch.bailu.aat_lib.coordinates.LatLongE6.Companion.toLatLong
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidMapPosition
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.LatLong

class MapPositionLayer(
    private val mcontext: MapContext,
    private val storage: StorageInterface,
    d: DispatcherInterface
) : MapLayerInterface, TargetInterface {
    private val slock: SolidPositionLock = SolidPositionLock(storage, mcontext.getSolidKey())
    private var gpsLocation = LatLong(0.0, 0.0)

    init {
        loadState()
        d.addTarget(this, InfoID.LOCATION)
    }

    fun onMapCenterChanged(center: LatLong) {
        if (gpsLocation != center) {
            disableLock()
        }
    }

    private fun disableLock() {
        slock.value = false
    }

    private fun loadState() {
        gpsLocation = SolidMapPosition.readPosition(storage, mcontext.getSolidKey()).toLatLong()
        val zoomLevel = SolidMapPosition.readZoomLevel(storage, mcontext.getSolidKey()).toByte()
        mcontext.getMapView().setZoomLevel(zoomLevel)
        mcontext.getMapView().setCenter(gpsLocation)
    }

    private fun setMapCenter() {
        if (slock.isEnabled) {
            mcontext.getMapView().setCenter(gpsLocation)
        }
    }

    private fun saveState() {
        val center = mcontext.getMapView().getMapViewPosition().center
        val zoomLevel = mcontext.getMapView().getMapViewPosition().zoomLevel.toInt()
        SolidMapPosition.writePosition(storage, mcontext.getSolidKey(), center)
        SolidMapPosition.writeZoomLevel(storage, mcontext.getSolidKey(), zoomLevel)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (slock.hasKey(key)) {
            setMapCenter()
        }
    }

    override fun onAttached() {}
    override fun onDetached() {
        saveState()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        gpsLocation = toLatLong(info)
        setMapCenter()
    }

    override fun drawInside(mcontext: MapContext) {}
    override fun drawForeground(mcontext: MapContext) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }
}
