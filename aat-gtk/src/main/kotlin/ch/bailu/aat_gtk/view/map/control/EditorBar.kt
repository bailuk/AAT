package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
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
        add(PopupButton(EditorMenu(edit, app)).apply {
            createActions(app)
            setIcon(Icons.openMenuSymbolic)
        }.overlay)

        add(Icons.listAddSymbolic).onClicked {
                val editor = edit.editor
                val point = mcontext.getMapView().getMapViewPosition().center
                val altitude = services.elevationService.getElevation(point.latitudeE6, point.longitudeE6).toFloat()
                editor.add(GpxPoint(point, altitude, 0))
        }

        add(Icons.listRemoveSymbolic).onClicked {
            edit.editor.remove()
        }

        add(Icons.goUpSymbolic).onClicked {
            edit.editor.up()
        }

        add(Icons.goDownSymbolic).onClicked {
            edit.editor.down()
        }

        add(Icons.editUndoSymbolic).onClicked {
            edit.editor.undo()
        }

        add(Icons.editRedoSymbolic).onClicked {
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
