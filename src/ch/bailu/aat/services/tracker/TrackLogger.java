package ch.bailu.aat.services.tracker;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;

public class TrackLogger extends Logger {
    final public static int MIN_TRACKPOINTS=5;

    private boolean requestSegment=true;
    private GpxList track=new GpxList(GpxBigDeltaInterface.TRK);

    final private File logFile;
    final private GpxListWriter writer;
    final private int presetIndex;
    final private Context context;


/*    public static File getLogFile() throws IOException {
        return new File(AppDirectory.getLogFile(context));
    }
*/
    
    public TrackLogger(Context c, int preset) throws IOException {
        context=c;
        presetIndex=preset;

        new TrackCrashRestorer(context, presetIndex);
        
        logFile = AppDirectory.getLogFile(context);

        writer=new GpxListWriter(track,logFile);

        setVisibleTrackSegment(track.getDelta());
    }


    @Override
    public String getPath() {
        return logFile.getPath();
    }

    @Override
    public String getName() {
        return logFile.getName();
    }


    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public GpxList getGpxList() {
        return track;
    }

    @Override
    public long getTimeDelta() {
        long diff;
        if (requestSegment) diff = 0;
        else diff = System.currentTimeMillis()-getEndTime();
        return super.getTimeDelta()+diff;            
    }


    public void logPause() throws IOException {
        if (track.getPointList().size()>0) {
            //requestSegment=false; FIXME ?????
            //log(new LastTrackPoint());
            requestSegment=true;
        }
    }


    public void log(GpxPointInterface tp) throws IOException {
        if (requestSegment) {
            requestSegment=false;
            track.appendToNewSegment(new GpxPoint(tp), GpxAttributes.NULL_ATTRIBUTES);

        } else {
            track.appendToCurrentSegment(new GpxPoint(tp), GpxAttributes.NULL_ATTRIBUTES);
        }

        setVisibleTrackPoint((GpxPointNode)track.getPointList().getLast());
        writer.flushOutput();
    }


    public void cleanUp() {
        try {
            writer.close();

            if (track.getPointList().size()>MIN_TRACKPOINTS) {
                logFile.renameTo(generateTargetFile(context, presetIndex));

            } else{
                logFile.delete();
            }

        } catch (IOException e) {
            AppLog.e(context, e);
        }

    }
    
    public static File generateTargetFile(Context context, int preset) throws IOException {
        return AppDirectory.generateUniqueFilePath(
                AppDirectory.getTrackListDirectory(context, preset),
                AppDirectory.generateDatePrefix(),
                AppDirectory.GPX_EXTENSION);
    }

/*
    private class LastTrackPoint implements GpxPointInterface {
        @Override
        public int getLatitudeE6()   {return TrackLogger.this.getLatitudeE6();}
        @Override
        public int getLongitudeE6()  {return TrackLogger.this.getLongitudeE6();}
        @Override
        public short getAltitude()   {return TrackLogger.this.getAltitude();}
        @Override
        public double getLongitude() {return TrackLogger.this.getLongitude();}
        @Override
        public double getLatitude()  {return TrackLogger.this.getLatitude();}
        @Override
        public long getTimeStamp()   {return System.currentTimeMillis();}
    }*/
}
