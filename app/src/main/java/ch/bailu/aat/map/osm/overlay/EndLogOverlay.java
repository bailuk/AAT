package ch.bailu.aat.map.osm.overlay;

import java.util.Locale;

import ch.bailu.aat.map.osm.OsmInteractiveView;

public class EndLogOverlay extends OsmOverlay {
    private long refreshTimeCounter=0;
    private long refreshCounter=0;

    private long currentTime=0;

    private final StartLogOverlay start;

    public EndLogOverlay(OsmInteractiveView v, StartLogOverlay s) {
        super(v);
        start = s;
    }

    @Override
    public void draw(MapPainter painter) {
        increment();
        log(painter);
    }


    private void increment() {
        refreshCounter++;
        currentTime= System.currentTimeMillis()- start.getStartTime();
        refreshTimeCounter += currentTime;

    }


    private void log(MapPainter painter) {
        double averageTime=
                ((double)refreshTimeCounter) /
                ((double)refreshCounter)	 /	1000d;
        double currentTime = ((double)this.currentTime / 1000d);

        painter.canvas.drawTextBottom(String.format((Locale)null,"%d: %1.3f s / %1.3f s",
                refreshCounter, 
                averageTime, 
                currentTime),5);
    }
}
