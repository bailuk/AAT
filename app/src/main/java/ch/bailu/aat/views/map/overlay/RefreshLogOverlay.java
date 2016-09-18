package ch.bailu.aat.views.map.overlay;

import java.util.Locale;

import ch.bailu.aat.views.map.OsmInteractiveView;

public class RefreshLogOverlay extends OsmOverlay {
    private long refreshTimeCounter=0;
    private long refreshCounter=0;

    private long currentTime=0;


    public RefreshLogOverlay(OsmInteractiveView v) {
        super(v);
    }

    @Override
    public void draw(MapPainter painter) {
        increment();
        log(painter);
    }


    private void increment() {
        refreshCounter++;
        currentTime= System.currentTimeMillis()-getOsmView().getDrawStartTime();
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
