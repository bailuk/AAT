package ch.bailu.aat.test;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.services.tracker.TrackLogger;
import ch.bailu.simpleio.foc.Foc;

public class TestGpxLogRecovery extends TestGpx {


    public TestGpxLogRecovery(Context c) {
        super(c);
    }

    
    @Override
    public void test() throws IOException, AssertionError  {
        /*
        Foc testFile = getTestFile();
        Foc logFile = AppDirectory.getDataDirectory(getContext(), AppDirectory.DIR_LOG).child("log.gpx");
        assertFalse(logFile.toString() + " is in use.", logFile.isReachable());
        
        
        copyFile(testFile, logFile);
        assertTrue("Test failed: '" + logFile.toString() + "' does not exist." ,logFile.isReachable());

        
        testFile(logFile, testFile);
        
        Foc targetDirectory =  AppDirectory.getTrackListDirectory(getContext(),new SolidPreset(this.getContext()).getIndex());
        assertEquals(true, targetDirectory.isDir());
        
        new TrackLogger(getContext(), new SolidPreset(this.getContext() ).getIndex()).close();
        assertEquals(logFile.isReachable(), false);
        
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
        testFile(targetFile, testFile);
        */
    }

    
    private void copyFile(Foc sourceFile, Foc destFile) throws IOException, AssertionError {
        /*
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
        }*/
    }    
    
}
