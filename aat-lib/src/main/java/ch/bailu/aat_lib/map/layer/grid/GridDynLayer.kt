package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point

class GridDynLayer(
    private val services: ServicesInterface,
    storage: StorageInterface,
    private val mcontext: MapContext
) : MapLayerInterface {
    private val sgrid = SolidMapGrid(storage, mcontext.getSolidKey())
    private var gridLayer = sgrid.createGridLayer(services)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {
        gridLayer.drawInside(mcontext)
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun drawForeground(mcontext: MapContext) {
        gridLayer.drawForeground(mcontext)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sgrid.hasKey(key)) {
            gridLayer = sgrid.createGridLayer(services)
            mcontext.getMapView().requestRedraw()
        }
    }

    override fun onAttached() {}
    override fun onDetached() {}
}
