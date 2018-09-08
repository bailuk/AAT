package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.util_java.foc.Foc;

public class GpxEditor {

    private final EditorRing ring;


    public GpxEditor(GpxList list) {
        if (list.getPointList().size() > 0) {
            ring = new EditorRing((new NodeEditor((GpxPointNode) list.getPointList().getFirst(),
                    list)));
        } else {
            ring = new EditorRing(new NodeEditor());
        }
    }


    public void select(GpxPointNode point, GpxList list) {
        ring.set(new NodeEditor(point, list));
    }


    public void clear() {
        ring.add(new NodeEditor());
    }


    public void unlinkSelectedNode() {
        ring.add(ring.get().unlink());

    }


    public void insertNode(GpxPointInterface point) {
        ring.add(ring.get().insert(point));

    }


    public void moveSelectedUp() {
        GpxPointInterface point = ring.get().getPoint();

        ring.add(ring.get().unlink());
        ring.set(ring.get().previous());
        ring.set(ring.get().insert(point));

    }


    public void moveSelectedDown() {
        GpxPointInterface point = ring.get().getPoint();

        ring.add(ring.get().unlink());
        ring.set(ring.get().next());
        ring.set(ring.get().insert(point));

    }


    public GpxList getList() {
        return ring.get().getList();
    }


    public GpxPointNode getSelectedPoint() {
        return ring.get().getPoint();
    }


    public void setType(GpxType type) {
        ring.get().getList().setType(type);
    }


    public boolean undo() {
        return ring.undo();
    }


    public boolean redo() {
        return ring.redo();
    }


    public void simplify() {
        ring.add(ring.get().simplify());
    }

    public void fix() {
        ring.add(ring.get().fix());
    }

    public void inverse() {
        ring.add(ring.get().inverse());
    }

    public void attach(GpxList toAttach) {
        ring.add(ring.get().attach(toAttach));
    }

}
