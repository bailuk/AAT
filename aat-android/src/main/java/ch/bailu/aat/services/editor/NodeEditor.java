package ch.bailu.aat.services.editor;

import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.tools.Attacher;
import ch.bailu.aat_lib.gpx.tools.Copier;
import ch.bailu.aat_lib.gpx.tools.Inverser;
import ch.bailu.aat_lib.gpx.tools.SimplifierBearing;
import ch.bailu.aat_lib.gpx.tools.SimplifierDistance;
import ch.bailu.aat_lib.gpx.tools.TimeStampFixer;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointFirstNode;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;

public final class NodeEditor {
    private final GpxList gpxList;
    private final GpxPointNode node;


    public NodeEditor() {
        this(GpxType.ROUTE);
    }

    public NodeEditor(GpxType t) {
        gpxList = new GpxList(t, GpxListAttributes.NULL);
        node = new GpxPointFirstNode(GpxPoint.NULL, GpxAttributesNull.NULL);
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

    public NodeEditor cutPreciding() {
        PrecedingCutter cutter = new PrecedingCutter();
        cutter.walkTrack(gpxList);

        return saveReturn(cutter.getNewNode());
    }


    private NodeEditor saveReturn(NodeEditor n) {
        if (n == null) return this;
        return n;
    }


    public NodeEditor cutRemaining() {
        RemainingCutter cutter = new RemainingCutter();
        cutter.walkTrack(gpxList);

        return saveReturn(cutter.getNewNode());
    }


    private class Unlinker extends GpxListWalker {
        private final GpxList newList = new GpxList(gpxList.getDelta().getType(), GpxListAttributes.NULL);

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
                GpxListAttributes.NULL);

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
                        GpxAttributesNull.NULL);
                newNode = insertNewPoint();
            }
        }

        public NodeEditor getNewNode() {
            if (newList.getPointList().size() == 0) {
                newList.appendToCurrentSegment(new GpxPoint(newPoint),
                        GpxAttributesNull.NULL);
                newNode = insertNewPoint();
            }
            return newNode;
        }

        private NodeEditor insertNewPoint() {
            return new NodeEditor(
                    (GpxPointNode) newList.getPointList().getLast(), newList);
        }
    }






    private class RemainingCutter extends GpxListWalker {
        private final GpxList newList = new GpxList(gpxList.getDelta().getType(), GpxListAttributes.NULL);

        private boolean startSegment=false;

        private boolean goOn=true;

        @Override
        public boolean doList(GpxList track) {
            return goOn;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            startSegment=true;
            return goOn;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return goOn;
        }

        @Override
        public void doPoint(GpxPointNode pointNode) {
            if (goOn) {
                copyNode(pointNode);
                if (pointNode == node) {
                    goOn = false;
                }
            }
        }

        public void copyNode(GpxPointNode pointNode) {
            if (startSegment) {
                newList.appendToNewSegment(pointNode.getPoint(), pointNode.getAttributes());
                startSegment=false;
            } else {
                newList.appendToCurrentSegment(pointNode.getPoint(), pointNode.getAttributes());
            }
        }


        public NodeEditor getNewNode() {
            if (newList.getPointList().size()>0) {
                return new NodeEditor(
                        (GpxPointNode)newList.getPointList().getLast(), newList);
            }
            return null;
        }
    }


    private class PrecedingCutter extends GpxListWalker {
        private final GpxList newList = new GpxList(gpxList.getDelta().getType(), GpxListAttributes.NULL);

        private boolean startSegment=false;
        private boolean start=false;

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
                start = true;

            }

            if (start) {
                copyNode(pointNode);
            }
        }

        public void copyNode(GpxPointNode pointNode) {
            if (startSegment) {
                newList.appendToNewSegment(pointNode.getPoint(), pointNode.getAttributes());
                startSegment=false;
            } else {
                newList.appendToCurrentSegment(pointNode.getPoint(), pointNode.getAttributes());
            }
        }


        public NodeEditor getNewNode() {
            if (newList.getPointList().size()>0) {
                return new NodeEditor(
                        (GpxPointNode)newList.getPointList().getFirst(), newList);
            }
            return null;
        }
    }

}
