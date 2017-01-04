package ch.bailu.aat.map.osmdroid.overlay;

import ch.bailu.aat.map.osmdroid.AbsOsmView;

public class StartLogOverlay extends OsmOverlay {
    private long time = System.currentTimeMillis();
    public StartLogOverlay(AbsOsmView o) {
        super(o);
    }

    @Override
    public void draw(MapPainter p) {
        time = System.currentTimeMillis();
    }

    public long getStartTime() {
        return time;
    }
}
