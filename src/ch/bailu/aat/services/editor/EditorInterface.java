package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;

public interface EditorInterface {
    EditorInterface NULL = new EditorInterface() {

        @Override
        public void save() {}

        @Override
        public void toggle() {}

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
        public void saveAs() {}

        @Override
        public void clear() {}

        @Override
        public void redo() {}

        @Override
        public void undo() {}

        @Override
        public void discard() {}
    };
    public void save();
    public void toggle();
    public void remove();
    public void add(GpxPoint point);
    public void up();
    public void down();
    public boolean isModified();

    public GpxPointNode getSelected();
    public void select(GpxPointNode p);

    public void saveAs();
    public void clear();
    public void redo();
    public void undo();
    public void discard();
}
