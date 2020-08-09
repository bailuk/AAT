package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.foc.Foc;

public interface EditorInterface {
    EditorInterface NULL = new EditorInterface() {

        @Override
        public void save() {}

        @Override
        public void setType(GpxType type) {}

        @Override
        public void remove() {}

        @Override
        public void add(GpxPoint point) {}

        @Override
        public void up() {}

        @Override
        public void down() {}

        @Override
        public boolean isModified() {
            return false;
        }

        @Override
        public GpxPointNode getSelected() {
            return null;
        }

        @Override
        public void select(GpxPointNode p) {}

        @Override
        public void saveTo(Foc path) {}

        @Override
        public void clear() {}

        @Override
        public void redo() {}

        @Override
        public void undo() {}

        @Override
        public void inverse() {}

        @Override
        public void attach(GpxList file) {}

        @Override
        public void fix() {}

        @Override
        public void simplify() {}

        @Override
        public void cutPreceding() {}

        @Override
        public void cutRemaining(){}

    };
    void save();
    void setType(GpxType type);
    void remove();
    void add(GpxPoint point);
    void up();
    void down();
    boolean isModified();

    GpxPointNode getSelected();
    void select(GpxPointNode p);

    void saveTo(Foc path);
    void clear();
    void redo();
    void undo();

    void inverse();
    void attach(GpxList toAttach);
    void fix();
    void simplify();
    void cutPreceding();
    void cutRemaining();
}
