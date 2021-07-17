package ch.bailu.aat_lib.service.tracker;

import java.io.IOException;

import javax.naming.Context;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.SolidFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.xml.writer.GpxListWriter;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class TrackLogger extends Logger {
    final public static int MIN_TRACKPOINTS=5;

    private boolean requestSegment=true;
    private final GpxList track;

    final private Foc logFile;
    final private GpxListWriter writer;
    final private int presetIndex;

    final SolidDataDirectory sdirectory;


    public TrackLogger(SolidDataDirectory sdirectory, int preset) throws IOException, SecurityException {
        presetIndex=preset;

        this.sdirectory = sdirectory;

        track = createTrack(sdirectory.getStorage(), presetIndex);
        new TrackCrashRestorer(sdirectory, presetIndex);

        logFile = AppDirectory.getLogFile(sdirectory);

        writer=new GpxListWriter(track,logFile);

        setVisibleTrackSegment(track.getDelta());
    }

    private static GpxList createTrack(StorageInterface s, int presetIndex) {
        SolidAutopause spause = new SolidPostprocessedAutopause(s, presetIndex);

        return new GpxList(GpxType.TRACK, GpxListAttributes.factoryTrack(spause));
    }

    @Override
    public Foc getFile() {
        return logFile;
    }


    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public GpxList getGpxList() {
        return track;
    }


    public void logPause() {
        if (track.getPointList().size()>0) {
            requestSegment=true;
        }
    }


    @Override
    public void log(GpxPointInterface tp, GpxAttributes attr) throws IOException {
        if (requestSegment) {
            requestSegment=false;
            track.appendToNewSegment(new GpxPoint(tp), attr);

        } else {
            track.appendToCurrentSegment(new GpxPoint(tp), attr);
        }

        setVisibleTrackPoint((GpxPointNode)track.getPointList().getLast());
        writer.flushOutput();
    }


    @Override
    public void close() {
        try {
            writer.close();

            if (track.getPointList().size() > MIN_TRACKPOINTS) {
                logFile.move(generateTargetFile(sdirectory, presetIndex));

            } else{
                logFile.remove();
            }

        } catch (IOException e) {
            // TODO error to gui
            AppLog.e(e);
        }

    }

    public static Foc generateTargetFile(SolidDataDirectory sdirectory, int preset) throws IOException {
        return AppDirectory.generateUniqueFilePath(
                AppDirectory.getTrackListDirectory(sdirectory, preset),
                AppDirectory.generateDatePrefix(),
                AppDirectory.GPX_EXTENSION);
    }
}
