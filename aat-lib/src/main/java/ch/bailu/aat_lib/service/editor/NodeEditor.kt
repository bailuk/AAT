package ch.bailu.aat_lib.service.editor

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointFirstNode
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.gpx.tools.Attacher
import ch.bailu.aat_lib.gpx.tools.Copier
import ch.bailu.aat_lib.gpx.tools.Inverser
import ch.bailu.aat_lib.gpx.tools.SimplifierBearing
import ch.bailu.aat_lib.gpx.tools.SimplifierDistance
import ch.bailu.aat_lib.gpx.tools.TimeStampFixer

class NodeEditor {
    val list: GpxList
    val point: GpxPointNode


    @JvmOverloads
    constructor(type: GpxType = GpxType.ROUTE) {
        this.list = GpxList(type, GpxListAttributes.NULL)
        this.point = GpxPointFirstNode(GpxPoint.NULL, GpxAttributesNull.NULL)
    }

    constructor(node: GpxPointNode, list: GpxList) {
        this.point = node
        this.list = list
    }

    fun simplify(): NodeEditor {
        val distance = SimplifierDistance()
        distance.walkTrack(this.list)

        val bearing = SimplifierBearing()
        val distanceList = distance.newList
        if (distanceList is GpxList) {
            bearing.walkTrack(distanceList)
            val bearingList = bearing.newList
            if (bearingList is GpxList) {
                return toEditor(bearingList)
            }
        }
        return this
    }

    fun fix(): NodeEditor {
        val fixer = TimeStampFixer()
        fixer.walkTrack(this.list)
        val list = fixer.newList
        if (list is GpxList) {
            return toEditor(list)
        }
        return this
    }


    fun inverse(): NodeEditor {
        val inverser = Inverser(this.list)
        return toEditor(inverser.newList)
    }

    fun attach(toAttach: GpxList): NodeEditor {
        val copier = Copier()
        copier.walkTrack(this.list)

        val attacher = Attacher(copier.newList!!)
        attacher.walkTrack(toAttach)

        return toEditor(attacher.newList)
    }

    fun unlink(): NodeEditor {
        val unlinker = Unlinker()
        unlinker.walkTrack(this.list)

        return unlinker.getNewNode()
    }

    fun insert(point: GpxPointInterface): NodeEditor {
        val inserter = Inserter(point)
        inserter.walkTrack(this.list)

        return inserter.getNewNode()
    }

    fun previous(): NodeEditor {
        val newNode = point.previous as GpxPointNode?

        if (newNode == null) return this
        return NodeEditor(newNode, this.list)
    }

    fun next(): NodeEditor {
        val newNode = point.next as GpxPointNode?

        if (newNode == null) return this
        return NodeEditor(newNode, this.list)
    }

    fun cutPreceding(): NodeEditor {
        val cutter = PrecedingCutter()
        cutter.walkTrack(this.list)

        return saveReturn(cutter.newNode)
    }

    private fun saveReturn(node: NodeEditor?): NodeEditor {
        if (node == null) return this
        return node
    }

    fun cutRemaining(): NodeEditor {
        val cutter = RemainingCutter()
        cutter.walkTrack(this.list)

        return saveReturn(cutter.newNode)
    }

    private inner class Unlinker : GpxListWalker() {
        private val newList = GpxList(list.getDelta().getType(), GpxListAttributes.NULL)

        private var startSegment = false
        private var newNode: NodeEditor? = null

        override fun doList(track: GpxList): Boolean {
            return true
        }

        override fun doSegment(segment: GpxSegmentNode): Boolean {
            startSegment = true
            return true
        }

        override fun doMarker(marker: GpxSegmentNode): Boolean {
            return true
        }

        override fun doPoint(pointNode: GpxPointNode) {
            if (pointNode === this@NodeEditor.point) {
                if (newList.pointList.size() > 0) {
                    newNode = NodeEditor(
                        (newList.pointList.last as GpxPointNode?)!!, newList
                    )
                }
            } else {
                if (startSegment) {
                    newList.appendToNewSegment(pointNode.point, pointNode.getAttributes())
                    startSegment = false
                } else {
                    newList.appendToCurrentSegment(pointNode.point, pointNode.getAttributes())
                }
            }
        }

        fun getNewNode(): NodeEditor {
            if (newNode == null) {
                if (newList.pointList.size() > 0) {
                    newNode = NodeEditor(
                        (newList.pointList.first as GpxPointNode?)!!, newList
                    )
                } else {
                    newNode = NodeEditor(newList.getDelta().getType())
                }
            }
            return newNode!!
        }
    }


    private inner class Inserter(private val newPoint: GpxPointInterface) : GpxListWalker() {
        private val newList = GpxList(
            list.getDelta().getType(),
            GpxListAttributes.NULL
        )

        private var newNode = NodeEditor(list.getDelta().getType())
        private var startSegment = false


        override fun doList(track: GpxList): Boolean {
            return true
        }

        override fun doSegment(segment: GpxSegmentNode): Boolean {
            startSegment = true
            return true
        }

        override fun doMarker(marker: GpxSegmentNode): Boolean {
            return true
        }

        override fun doPoint(point: GpxPointNode) {
            if (startSegment) {
                newList.appendToNewSegment(point.point, point.getAttributes())
                startSegment = false
            } else {
                newList.appendToCurrentSegment(point.point, point.getAttributes())
            }

            if (point === this@NodeEditor.point) {
                newList.appendToCurrentSegment(
                    GpxPoint(newPoint),
                    GpxAttributesNull.NULL
                )
                newNode = insertNewPoint()
            }
        }

        fun getNewNode(): NodeEditor {
            if (newList.pointList.size() == 0) {
                newList.appendToCurrentSegment(
                    GpxPoint(newPoint),
                    GpxAttributesNull.NULL
                )
                newNode = insertNewPoint()
            }
            return newNode
        }

        fun insertNewPoint(): NodeEditor {
            return NodeEditor(
                (newList.pointList.last as GpxPointNode?)!!, newList
            )
        }
    }


    private inner class RemainingCutter : GpxListWalker() {
        private val newList = GpxList(list.getDelta().getType(), GpxListAttributes.NULL)

        private var startSegment = false

        private var goOn = true

        override fun doList(track: GpxList): Boolean {
            return goOn
        }

        override fun doSegment(segment: GpxSegmentNode): Boolean {
            startSegment = true
            return goOn
        }

        override fun doMarker(marker: GpxSegmentNode): Boolean {
            return goOn
        }

        override fun doPoint(pointNode: GpxPointNode) {
            if (goOn) {
                copyNode(pointNode)
                if (pointNode === this@NodeEditor.point) {
                    goOn = false
                }
            }
        }

        fun copyNode(pointNode: GpxPointNode) {
            if (startSegment) {
                newList.appendToNewSegment(pointNode.point, pointNode.getAttributes())
                startSegment = false
            } else {
                newList.appendToCurrentSegment(pointNode.point, pointNode.getAttributes())
            }
        }


        val newNode: NodeEditor?
            get() {
                if (newList.pointList.size() > 0) {
                    return NodeEditor(
                        (newList.pointList.last as GpxPointNode?)!!, newList
                    )
                }
                return null
            }
    }


    private inner class PrecedingCutter : GpxListWalker() {
        private val newList = GpxList(list.getDelta().getType(), GpxListAttributes.NULL)

        private var startSegment = false
        private var start = false

        override fun doList(track: GpxList): Boolean {
            return true
        }

        override fun doSegment(segment: GpxSegmentNode): Boolean {
            startSegment = true
            return true
        }

        override fun doMarker(marker: GpxSegmentNode): Boolean {
            return true
        }

        override fun doPoint(pointNode: GpxPointNode) {
            if (pointNode === this@NodeEditor.point) {
                start = true
            }

            if (start) {
                copyNode(pointNode)
            }
        }

        fun copyNode(pointNode: GpxPointNode) {
            if (startSegment) {
                newList.appendToNewSegment(pointNode.point, pointNode.getAttributes())
                startSegment = false
            } else {
                newList.appendToCurrentSegment(pointNode.point, pointNode.getAttributes())
            }
        }

        val newNode: NodeEditor?
            get() {
                if (newList.pointList.size() > 0) {
                    return NodeEditor(
                        (newList.pointList.first as GpxPointNode?)!!, newList
                    )
                }
                return null
            }
    }

    companion object {
        private fun toEditor(list: GpxList): NodeEditor {
            if (list.pointList.size() > 0) {
                return NodeEditor(
                    (list.pointList.first as GpxPointNode?)!!, list
                )
            } else {
                return NodeEditor(list.getDelta().getType())
            }
        }
    }
}
