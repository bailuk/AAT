package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Orientation

class EditBar(private val scontext: ServicesInterface, mcontext: MapContext, edit: EditorSourceInterface) {
    val bar = Bar(Orientation.VERTICAL).apply {
        add("open-menu-symbolic")
        add("list-add-symbolic").apply {
            onClicked {
                AppLog.d(this, "onClicked()")

                val editor = edit.editor
                val point = mcontext.mapView.mapViewPosition.center
                val altitude = scontext.elevationService.getElevation(point.latitudeE6, point.longitudeE6).toFloat()
                editor.add(GpxPoint(point, altitude, 0))
            }
        }
    }

    private val remove = bar.add("list-remove-symbolic")

    private val up = bar.add("go-up-symbolic")
    private val down = bar.add("go-down-symbolic")
    private val undo = bar.add("edit-undo-symbolic")
    private val redo = bar.add("edit-redo-symbolic")
}
