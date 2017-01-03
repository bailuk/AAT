package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.preferences.SolidUnit;

public class DistanceAltitudeGraphView extends AbsGraphView {

    
    public DistanceAltitudeGraphView(Context context, DispatcherInterface di, int iid) {
        super(context, di, iid);
    }

    @Override
    public void plot(Canvas canvas, GpxList list, SolidUnit sunit, boolean markerMode) {
        int km_factor = (int) (list.getDelta().getDistance()/1000) + 1;
        GraphPlotter plotter = new GraphPlotter(canvas,getWidth(), getHeight(), 1000 * km_factor,
                new AppDensity(getContext()));
        GpxListWalker painter, scaleGenerator;
        
        if (markerMode) {
            painter = new GraphPainterMarkerMode(plotter, (int)list.getDelta().getDistance() / getWidth());
            scaleGenerator = new ScaleGeneratorMarkerMode(plotter);
        } else {
            painter = new GraphPainter(plotter, (int)list.getDelta().getDistance() / getWidth());
            scaleGenerator = new ScaleGenerator(plotter);

        }
        
        
        plotter.drawXScale(5, 
                plotterLabel(R.string.distance, sunit.getDistanceUnit()), 
                sunit.getDistanceFactor());
        
        
        scaleGenerator.walkTrack(list);
        plotter.roundYScale(50);
        
        plotter.drawYScale(5, 
                plotterLabel(R.string.altitude, sunit.getAltitudeUnit()), 
                sunit.getAltitudeFactor());

        painter.walkTrack(list);
    }


    
    private class ScaleGenerator extends GpxListWalker {
        private final GraphPlotter plotter;
        
        public ScaleGenerator(GraphPlotter p) {
            plotter=p;
        }
        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            plotter.inlcudeInYScale((float)point.getAltitude());            
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return true;
        }

        @Override
        public boolean doList(GpxList track) {
            return true;
        }
        
    }

    
    private class ScaleGeneratorMarkerMode extends ScaleGenerator {

        public ScaleGeneratorMarkerMode(GraphPlotter p) {
            super(p);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            GpxPointNode point = (GpxPointNode) marker.getFirstNode();
            if (point != null) {
                doPoint(point);
            }
            return false;
        }
    }
    


    private class GraphPainter extends GpxListWalker {
        private final GraphPlotter plotter;
        
        private float distance=0;
        private float summaryDistance=0;
        private final float minDistance;
        

        public GraphPainter(GraphPlotter p, int md) {
            plotter=p;
            minDistance=md*SAMPLE_WIDTH_PIXEL;
        }
        
        
        
        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        
        @Override
        public void doPoint(GpxPointNode point) {
            incrementSummaryDistance(point.getDistance());
            plotIfDistance(point);
        }

        
        public void incrementSummaryDistance(float distance) {
            summaryDistance += distance;            
        }

        public void plotIfDistance(GpxPointNode point) {
            if (summaryDistance >= minDistance) {
                int altitude = (int)point.getAltitude();
                
                distance+=summaryDistance;
                summaryDistance=0;
                
                plotter.plotData(distance, altitude, ColorTable.altitude.getColor(altitude));
            }
        }



        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return true;
        }

        @Override
        public boolean doList(GpxList track) {
            return true;
        }
        
    }

    
    
    private class GraphPainterMarkerMode extends GraphPainter {

        public GraphPainterMarkerMode(GraphPlotter p, int md) {
            super(p, md);
        }
        

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            if (marker.getFirstNode() != null) {
                plotIfDistance((GpxPointNode)marker.getFirstNode());
                incrementSummaryDistance(marker.getDistance());
            }
            return false;
        }
    }
    
}
