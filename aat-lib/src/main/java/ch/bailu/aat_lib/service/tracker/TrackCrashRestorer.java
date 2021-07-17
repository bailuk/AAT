package ch.bailu.aat_lib.service.tracker;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.xml.writer.GpxListWriter;
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class TrackCrashRestorer{

    public TrackCrashRestorer (SolidDataDirectory sdirectory, int presetIndex) throws IOException {

        Foc source = AppDirectory.getLogFile(sdirectory);

        if (source.exists()) {

            GpxList track = readFile(source);
            if (track.getPointList().size() > TrackLogger.MIN_TRACKPOINTS) {
                // TODO message to gui
                AppLog.i(this, Res.str().tracker_restore());

                retstoreFile(sdirectory,track, presetIndex);

            }
            source.rm();
        }

    }


    private void retstoreFile(SolidDataDirectory sdirectory, GpxList track, int presetIndex) throws IOException {
        Foc target = TrackLogger.generateTargetFile(sdirectory, presetIndex);

        GpxListWriter writer=new GpxListWriter(track,target);
        writer.close();

    }







    private GpxList readFile(Foc remainingLogFile) {
        GpxListReader reader = new GpxListReader(remainingLogFile, AutoPause.NULL);
        return reader.getGpxList();
    }
}
