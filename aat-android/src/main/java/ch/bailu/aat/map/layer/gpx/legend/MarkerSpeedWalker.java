package ch.bailu.aat.map.layer.gpx.legend;

import android.content.Context;

import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.SpeedDescription;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public final class MarkerSpeedWalker extends LegendWalker {
    private final SpeedDescription description;

    public MarkerSpeedWalker(Context context) {
        description = new CurrentSpeedDescription(context);
    }


    float speed;
    int samples;

    @Override
    public boolean doList(GpxList l) {
        speed=0;
        samples=0;
        return super.doList(l);
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        boolean isLast = marker.getNext() == null;

        if (!isLast) {
            c.setB((GpxPointNode)marker.getFirstNode());

            if (!c.arePointsTooClose()) {
                if (samples >0) speed = speed / samples;

                drawLegendFromB();
                c.switchNodes();

                speed=0;
                samples=0;

            }

        }

        speed = speed + marker.getSpeed();
        samples++;

        return isLast;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        if (point.getNext()==null) {
            c.setB(point);

            if (!c.arePointsTooClose()) {
                speed = speed / samples;
            }
            drawLegendFromB();
        }
    }

    private void drawLegendFromB() {
        if (c.isBVisible()) {
            c.drawNodeB();
            if (!c.arePointsTooClose()) {
                c.drawLabelB(description.getSpeedDescription(speed));
            }
        }
    }
}
