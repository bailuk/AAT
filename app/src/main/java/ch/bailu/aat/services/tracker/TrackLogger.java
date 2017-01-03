package ch.bailu.aat.services.tracker;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppLog;

public class TrackLogger extends Logger {
    final public static int MIN_TRACKPOINTS=5;

    private boolean requestSegment=true;
    private final GpxList track=new GpxList(GpxType.TRK, new MaxSpeed.Samples());

    final private File logFile;
    final private GpxListWriter writer;
    final private int presetIndex;
    final private Context context;


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


    public void logPause() throws IOException {
        if (track.getPointList().size()>0) {
            requestSegment=true;
        }
    }


    public void log(GpxPointInterface tp) throws IOException {
        if (requestSegment) {
            requestSegment=false;
            track.appendToNewSegment(new GpxPoint(tp), GpxAttributesStatic.NULL_ATTRIBUTES);

        } else {
            track.appendToCurrentSegment(new GpxPoint(tp), GpxAttributesStatic.NULL_ATTRIBUTES);
        }

        setVisibleTrackPoint((GpxPointNode)track.getPointList().getLast());
        writer.flushOutput();
    }


    @Override
    public void close() {
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
}
