package ch.bailu.aat.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.services.tracker.TrackLogger;

public class TestGpxLogRecovery extends TestGpx {


    public TestGpxLogRecovery(Context c) {
        super(c);
    }

    
    @Override
    public void test() throws IOException, AssertionError  {
        SolidString mockLocation = new SolidMockLocationFile(getContext());
        
        File testFile = new File(mockLocation.getValue());
        File logFile = new File(AppDirectory.getDataDirectory(getContext(), AppDirectory.DIR_LOG),"log.gpx");
        
        copyFile(testFile, logFile);
        assertEquals(logFile.exists(), true);

        
        long logSize = logFile.length();
        long testSize = testFile.length();
        assertEquals(testSize, logSize);
        
        File targetDirectory =  AppDirectory.getTrackListDirectory(getContext(),new SolidPreset(this.getContext()).getIndex());
        assertEquals(true, targetDirectory.isDirectory());
        
        new TrackLogger(getContext(), new SolidPreset(this.getContext() ).getIndex()).close();
        assertEquals(logFile.exists(), false);
        
        String fileList[] = targetDirectory.list();
        
        File targetFile = null;
        for (String fileName: fileList) {
            File file = new File(targetDirectory, fileName);
            
            if (targetFile == null ||
                file.lastModified() > targetFile.lastModified()) {
                targetFile=file;
            }
            
        }

        
        assertNotNull(targetFile);
        assertEquals(true, targetFile.exists());
        testFile(targetFile, 2869, 1);
        assertEquals(true, targetFile.delete());
        assertEquals(false, targetFile.exists());        
    }

    
    private void copyFile(File sourceFile, File destFile) throws IOException, AssertionError {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }    
    
}
