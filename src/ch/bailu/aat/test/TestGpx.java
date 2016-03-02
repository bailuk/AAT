package ch.bailu.aat.test;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.parser.SimpleGpxListReader;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.preferences.SolidString;

public class TestGpx extends UnitTest {

 

    public TestGpx(Context c) {
        super(c);
    }


    @Override
    public void test() throws IOException, AssertionError {
        File testFile = getTestFile();
        
        testFile(testFile, testFile);
    }

    
    
    



    public File getTestFile() {
        SolidString mockLocation = new SolidMockLocationFile(getContext());
        
        File testFile = new File(mockLocation.getValue());
        assertTrue("Mock file not defined.", testFile.exists());
        return testFile;
    }


    public void testFile(File fileA, File fileB) throws IOException, AssertionError {
            GpxList listA= new SimpleGpxListReader(fileA).getGpxList();
            GpxList listB=new SimpleGpxListReader(fileB).getGpxList();
            
            File fileCopy = new File(getTestDirectory(getContext()),"test_copy.gpx");
            GpxListWriter writer = new GpxListWriter(listA, fileCopy);
            writer.flushOutput();
            writer.close();
            
            GpxList listC=new SimpleGpxListReader(fileCopy).getGpxList();
            

            assertListEquals(listA, listB);
            assertListEquals(listB, listC);
            
    }


    public static void assertListEquals(GpxList listA, GpxList listB) {
        assertEquals(listA.getPointList().size(), listB.getPointList().size());
        assertEquals(listA.getSegmentList().size(), listB.getSegmentList().size());
        assertEquals(listA.getMarkerList().size(), listB.getMarkerList().size());
        
        assertEquals(listA.getDelta().getEndTime(), listB.getDelta().getEndTime());
        assertEquals(listA.getDelta().getStartTime(), listB.getDelta().getStartTime());
        assertEquals(listA.getDelta().getPause(), listB.getDelta().getPause());
        
    }
    
    
    
}
