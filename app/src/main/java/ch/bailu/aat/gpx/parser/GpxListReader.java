package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.helpers.file.AbsAccess;
import ch.bailu.aat.services.background.ThreadControl;

public class GpxListReader {
    private final ThreadControl threadControl;

    private final OnParsed way = new OnParsed(GpxType.WAY);
    private final OnParsed track = new OnParsed(GpxType.TRK);
    private final OnParsed route = new OnParsed(GpxType.RTE);



    
    private XmlParser parser;

    public GpxListReader (ThreadControl c, AbsAccess f) throws IOException {
        threadControl=c;

        parser = new XmlParser(f);
        parser.setOnRouteParsed(route);
        parser.setOnTrackParsed(track);
        parser.setOnWayParsed(way);
        parser.parse();
        parser.close();

    }


    public GpxList getGpxList() {
        if (track.hasContent()) return track.getGpxList();
        if (route.hasContent()) return route.getGpxList();
        return way.getGpxList();
    }


    
    private class OnParsed implements OnParsedInterface {
        private final GpxList gpxList;
        private boolean  haveNewSegment=true;

        public OnParsed(int type) {
            gpxList = new GpxList(type, new MaxSpeed.Samples());
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
