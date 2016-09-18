package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;

public class GpxEditor {

    private final EditorRing ring; 
    
    
    public GpxEditor(GpxList list) {
        if (list.getPointList().size()>0) {
            ring = new EditorRing((new NodeEditor((GpxPointNode)list.getPointList().getFirst(), 
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


    public void toggleType() {
        int type = ring.get().getList().getDelta().getType();

        if (type == GpxType.WAY) {
            type = GpxType.RTE;
        } else {
            type = GpxType.WAY;
        }
        ring.get().getList().setType(type);
    }


    public boolean undo() {
        return ring.undo();
    }


    public boolean redo() {
        return ring.redo();
    }

}
