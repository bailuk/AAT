package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.AltitudeDelta;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointFirstNode;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.tools.Attacher;
import ch.bailu.aat.gpx.tools.Copier;
import ch.bailu.aat.gpx.tools.Inverser;
import ch.bailu.aat.gpx.tools.TimeStampFixer;
import ch.bailu.aat.gpx.tools.SimplifierBearing;
import ch.bailu.aat.gpx.tools.SimplifierDistance;

public class NodeEditor {
    private final GpxList gpxList;
    private final GpxPointNode node;


    public NodeEditor() {
        this(GpxType.ROUTE);
    }

    public NodeEditor(GpxType t) {
        gpxList = new GpxList(t, MaxSpeed.NULL, AutoPause.NULL, AltitudeDelta.NULL);
        node = new GpxPointFirstNode(GpxPoint.NULL, GpxAttributesStatic.NULL_ATTRIBUTES);
    }

    public NodeEditor(GpxPointNode n, GpxList l) {
        node = n;
        gpxList = l;
    }


    public GpxPointNode getPoint() {
        return node;
    }


    public GpxList getList() {
        return gpxList;
    }

    public NodeEditor simplify() {
        SimplifierDistance distance = new SimplifierDistance();
        distance.walkTrack(gpxList);

        SimplifierBearing bearing = new SimplifierBearing();
        bearing.walkTrack(distance.getNewList());

        return toEditor(bearing.getNewList());
    }



    public NodeEditor fix() {
        TimeStampFixer fixer = new TimeStampFixer();
        fixer.walkTrack(gpxList);

        return toEditor(fixer.getNewList());
    }


    public NodeEditor inverse() {
        Inverser inverser = new Inverser(gpxList);

        return toEditor(inverser.getNewList());
    }

    public NodeEditor attach(GpxList toAttach) {
        Copier copier = new Copier();
        copier.walkTrack(gpxList);

        Attacher attacher = new Attacher(copier.getNewList());
        attacher.walkTrack(toAttach);

        return toEditor(attacher.getNewList());
    }


    private static NodeEditor toEditor(GpxList list) {
        if (list.getPointList().size()>0) {
            return new NodeEditor(
                    (GpxPointNode)list.getPointList().getFirst(), list);
        } else {
            return new NodeEditor(list.getDelta().getType());
        }
    }

    public NodeEditor unlink() {
        Unlinker unlinker = new Unlinker();
        unlinker.walkTrack(gpxList);

        return unlinker.getNewNode();
    }


    public NodeEditor insert(GpxPointInterface point) {
        Inserter inserter = new Inserter(point);
        inserter.walkTrack(gpxList);

        return inserter.getNewNode();
    }


    public NodeEditor previous() {
        GpxPointNode newNode = (GpxPointNode)node.getPrevious();

        if (newNode == null) return this;
        return new NodeEditor(newNode, gpxList);
    }


    public NodeEditor next() {
        GpxPointNode newNode = (GpxPointNode)node.getNext();

        if (newNode == null) return this;
        return new NodeEditor(newNode, gpxList);
    }



    private class Unlinker extends GpxListWalker {
        private final GpxList newList = new GpxList(gpxList.getDelta().getType(),
                MaxSpeed.NULL,
                AutoPause.NULL,
                AltitudeDelta.NULL);

        private boolean startSegment=false;
        private NodeEditor newNode = null;

        @Override
        public boolean doList(GpxList track) {
            return true;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            startSegment=true;
            return true;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        @Override
        public void doPoint(GpxPointNode pointNode) {
            if (pointNode == node) {
                if (newList.getPointList().size()>0) {
                    newNode = new NodeEditor(
                            (GpxPointNode)newList.getPointList().getLast(), newList);
                }
            } else {
                if (startSegment) {
                    newList.appendToNewSegment(pointNode.getPoint(), pointNode.getAttributes());
                    startSegment=false;
                } else {
                    newList.appendToCurrentSegment(pointNode.getPoint(), pointNode.getAttributes());
                }
            }
        }

        public NodeEditor getNewNode() {
            if (newNode == null) {
                if (newList.getPointList().size()>0) {
                    newNode = new NodeEditor(
                            (GpxPointNode)newList.getPointList().getFirst(), newList);
                } else {
                    newNode = new NodeEditor(newList.getDelta().getType());
                }
            }
            return newNode;
        }
    }


    private class Inserter extends GpxListWalker {
        private final GpxList newList = new GpxList(gpxList.getDelta().getType(),
                MaxSpeed.NULL, AutoPause.NULL, AltitudeDelta.NULL);

        private NodeEditor newNode = new NodeEditor(gpxList.getDelta().getType());
        private boolean startSegment=false;
        private final GpxPointInterface newPoint;


        public Inserter(GpxPointInterface point) {
            newPoint = point;
        }

        @Override
        public boolean doList(GpxList track) {
            return true;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            startSegment=true;
            return true;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            if (startSegment) {
                newList.appendToNewSegment(point.getPoint(), point.getAttributes());
                startSegment=false;
            } else {
                newList.appendToCurrentSegment(point.getPoint(), point.getAttributes());
            }

            if (point == node) {
                newList.appendToCurrentSegment(new GpxPoint(newPoint),
                        GpxAttributesStatic.NULL_ATTRIBUTES);
                newNode = insertNewPoint();
            }
        }

        public NodeEditor getNewNode() {
            if (newList.getPointList().size() == 0) {
                newList.appendToCurrentSegment(new GpxPoint(newPoint),
                        GpxAttributesStatic.NULL_ATTRIBUTES);
                newNode = insertNewPoint();
            }
            return newNode;
        }

        private NodeEditor insertNewPoint() {
            return new NodeEditor(
                    (GpxPointNode) newList.getPointList().getLast(), newList);
        }
    }
}
