package ch.bailu.aat_lib.service.editor;

public final class EditorRing {
    private final NodeEditor[] ring = new NodeEditor[10];
    private int index=0;
    private int undoable=0;
    private int redoable=0;


    public EditorRing(NodeEditor s) {
        set(s);
    }


    public void add(NodeEditor s) {
        index++; if (index >= ring.length) index = 0;

        undoable = Math.min(undoable+1, ring.length);
        redoable=0;

        ring[index] = s;
    }

    public void set(NodeEditor s) {
        ring[index] = s;
    }

    public NodeEditor get() {
        return ring[index];
    }


    public boolean undo() {
        if (undoable > 0) {
            undoable--; redoable++;
            index--;    if (index < 0) index = ring.length-1;
            return true;
        }
        return false;
    }


    public boolean redo() {
        if (redoable > 0) {
            undoable++;
            redoable--;
            index = (index+1) % ring.length;
            return true;
        }
        return false;
    }
}
