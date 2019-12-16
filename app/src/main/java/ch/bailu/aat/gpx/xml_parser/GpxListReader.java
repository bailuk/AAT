package ch.bailu.aat.gpx.xml_parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.attributes.AutoPause;
import ch.bailu.aat.gpx.attributes.GpxListAttributes;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.xml_parser.parser.AbsXmlParser;
import ch.bailu.aat.services.background.ThreadControl;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.parser.OnParsedInterface;

public class GpxListReader {
    private final ThreadControl threadControl;

    private final OnParsed way;
    private final OnParsed track;
    private final OnParsed route;

    private AbsXmlParser parser = null;

    private Exception parserException = null;



    public GpxListReader(Foc in, AutoPause apause) {
        this(ThreadControl.KEEP_ON, in, apause);
    }


    public GpxListReader (ThreadControl c, Foc in, AutoPause apause) {
        this(c, in, GpxListAttributes.factoryTrack(apause));
    }


    private GpxListReader (ThreadControl c, Foc in, GpxListAttributes trackAttributes) {

        track = new OnParsed(GpxType.TRACK, trackAttributes);
        way   = new OnParsed(GpxType.WAY,   GpxListAttributes.NULL);
        route = new OnParsed(GpxType.ROUTE, GpxListAttributes.factoryRoute());

        threadControl=c;

        try {
            parser = new XmlParser(in);

            parser.setOnRouteParsed(route);
            parser.setOnTrackParsed(track);
            parser.setOnWayParsed(way);

            parser.parse();
            parser.close();

        } catch (Exception e) {
            parserException = e;
        }

    }


    public boolean hasParserException() {
        return parserException != null;
    }

    public Exception getException() {
        return parserException;
    }


    public GpxList getGpxList() {
        if (track.hasContent()) return track.getGpxList();
        if (route.hasContent()) return route.getGpxList();
        return way.getGpxList();
    }



    private class OnParsed implements OnParsedInterface {
        private final GpxList gpxList;
        private boolean  haveNewSegment=true;

        public OnParsed(GpxType type, GpxListAttributes attr) {
            gpxList = new GpxList(type, attr);
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
