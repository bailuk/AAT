package ch.bailu.aat.services.tracker;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.parser.SimpleGpxListReader;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.file.AbsAccess;
import ch.bailu.aat.helpers.file.FileAccess;


public class TrackCrashRestorer{
    

        
    
    
    public TrackCrashRestorer (Context context, int presetIndex) throws IOException {
        
        File source = AppDirectory.getLogFile(context);

        if (source.exists()) {
            
            GpxList track = readFile(new FileAccess(source));
            if (track.getPointList().size() > TrackLogger.MIN_TRACKPOINTS) { 
                AppLog.i(context, "Restore track*");                
        
                retstoreFile(context,track, presetIndex);
                
            }
            source.delete();
        }

    }

    
    private void retstoreFile(Context context, GpxList track, int presetIndex) throws IOException {
        File target = TrackLogger.generateTargetFile(context, presetIndex);
        
        GpxListWriter writer=new GpxListWriter(track,target);
        writer.close();

    }


    
    


    
    private GpxList readFile(AbsAccess remainingLogFile) throws IOException {
        SimpleGpxListReader reader = new SimpleGpxListReader(remainingLogFile);
        return reader.getGpxList();
    }
}
