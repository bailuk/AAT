package ch.bailu.aat.gpx.parser;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.services.background.ThreadControl;

public class GpxListReader {
    private ThreadControl threadControl;

    private OnParsed way = new OnParsed(GpxBigDelta.WAY), 
            track = new OnParsed(GpxBigDelta.TRK), 
            route = new OnParsed(GpxBigDelta.RTE);



    
    private XmlParser parser;

    public GpxListReader (ThreadControl c, File f) throws IOException {
        threadControl=c;

        parser = new XmlParser(f);
        parser.setOnRouteParsed(route);
        parser.setOnTrackParsed(track);
        parser.setOnWayParsed(way);
        parser.parse();
        parser.cleanUp();

    }


    public GpxList getGpxList() {
        if (track.hasContent()) return track.getGpxList();
        if (route.hasContent()) return route.getGpxList();
        return way.getGpxList();
    }


    
    private class OnParsed implements OnParsedInterface {
        private GpxList gpxList;
        private boolean  haveNewSegment=true;

        public OnParsed(int type) {
            gpxList = new GpxList(type);
        }


        public GpxList getGpxList() {
            return gpxList;
        }

        public boolean hasContent() {
            return gpxList.getPointList().size()>0;
        }

        @Override
        public void onHaveSegment() {
            haveNewSegment=true;
        }

        @Override
        public void onHavePoint() throws IOException {
            if (threadControl.canContinue()) {
                if (haveNewSegment) {
                    gpxList.appendToNewSegment(new GpxPoint(parser), 
                            parser.getAttributes());
                    haveNewSegment=false;
                } else {
                    gpxList.appendToCurrentSegment(new GpxPoint(parser), 
                            parser.getAttributes());

                }

            } else {
                throw new IOException();
            }
        }
    }
}
