package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.view.menu.PopupButton
import ch.bailu.aat_gtk.view.menu.provider.EditorMenu
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.selector.OnNodeSelectedInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Application

class EditorBar(app: Application,
                private val nodeInfo: NodeInfo,
                mcontext: MapContext,
                services: ServicesInterface,
                private val edit: EditorSourceInterface
) : Bar(Position.LEFT), OnNodeSelectedInterface {
    init {
        add(PopupButton(app, EditorMenu(edit, app)).apply { setIcon("open-menu-symbolic") }.overlay)

        add("list-add-symbolic").onClicked {
                val editor = edit.editor
                val point = mcontext.mapView.mapViewPosition.center
                val altitude = services.elevationService.getElevation(point.latitudeE6, point.longitudeE6).toFloat()
                editor.add(GpxPoint(point, altitude, 0))
        }

        add("list-remove-symbolic").onClicked {
            edit.editor.remove()
        }

        add("go-up-symbolic").onClicked {
            edit.editor.up()
        }

        add("go-down-symbolic").onClicked {
            edit.editor.down()
        }

        add("edit-undo-symbolic").onClicked {
            edit.editor.undo()
        }

        add("edit-redo-symbolic").onClicked {
            edit.editor.redo()
        }
    }

    override fun hide() {
        super.hide()
        nodeInfo.hide()
    }

    override fun show() {
        super.show()
        nodeInfo.showCenter()
    }

    override fun onNodeSelected(IID: Int, info: GpxInformation, node: GpxPointNode, index: Int) {
        edit.editor.select(node)
        nodeInfo.displayNode(info, node, index)
    }
}
