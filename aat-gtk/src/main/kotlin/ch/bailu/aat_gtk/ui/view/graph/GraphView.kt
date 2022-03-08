package ch.bailu.aat_gtk.ui.view.graph

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.view.graph.LabelInterface
import ch.bailu.aat_lib.view.graph.Plotter
import ch.bailu.aat_lib.view.graph.PlotterConfig
import ch.bailu.gtk.GTK
import ch.bailu.gtk.cairo.Context
import ch.bailu.gtk.glib.Glib
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Pointer

class GraphView(private val plotter: Plotter) : OnContentUpdatedInterface {
    private val drawingArea = DrawingArea()

    private var _width = 0
    private var _height = 0
    private var nodeIndex = -1

    private var gpxCache = GpxList.NULL_TRACK

    private val _labels = GraphLabel()

    var height: Int
        set(height) {drawingArea.contentHeight = height}
        get() = drawingArea.height


    val overlay = Overlay()

    private val plotterConfig = object : PlotterConfig {
        override fun getWidth(): Int {
            return _width
        }

        override fun getHeight(): Int {
            return _height
        }

        override fun getList(): GpxList {
            return gpxCache
        }

        override fun getIndex(): Int {
            return nodeIndex
        }

        override fun isXLabelVisible(): Boolean {
            return true
        }

        override fun getLabels(): LabelInterface {
            return _labels
        }
    }

    init {
        drawingArea.vexpand = GTK.TRUE
        drawingArea.hexpand = GTK.TRUE

        drawingArea.setDrawFunc({ _: DrawingArea, cr: Context, w: Int, h: Int, _: Pointer? ->
            _width = w
            _height = h
            plotter.plot(GtkCanvas(cr), plotterConfig)
            println("$w $h")

        }, null, {})


        overlay.child = drawingArea
        overlay.addOverlay(_labels.layout)

        plotter.initLabels(_labels)
    }


    private var redrawNeeded = false

    fun repaint() {
        /**
         * Repaint requests are coming from the main (UI) thread as well as from
         * the layer manager worker thread.
         * Functions from the gtk namespace do not support calls from outside the main (UI) thread.
         * Glib.idleAdd will add a callback to the main (UI) threads event system.
         * This callback will then call queueDraw() from within the main (UI) thread.
         */
        redrawNeeded = true
        Glib.idleAdd({
            if (redrawNeeded) {
                redrawNeeded = false
                drawingArea.queueDraw()
            }
            GTK.FALSE
        }, null)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        gpxCache = info.gpxList
    }
}
