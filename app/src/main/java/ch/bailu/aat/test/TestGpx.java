package ch.bailu.aat.test;

import android.content.Context;

import java.io.IOException;

import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.parser.GpxListReader;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public class TestGpx extends UnitTest {

 

    public TestGpx(Context c) {
        super(c);
    }


    @Override
    public void test() throws IOException, AssertionError {
        Foc testFile = getTestFile();
        
        testFile(testFile, testFile);
    }

    
    
    



    public Foc getTestFile() {
        SolidString mockLocation = new SolidMockLocationFile(getContext());
        
        Foc testFile = FocAndroid.factory(getContext(), mockLocation.getValueAsString());
        assertTrue("Mock file not defined.", testFile.exists());
        return testFile;
    }



    public void testFile(Foc fileA, Foc fileB) throws IOException, AssertionError {
            GpxList listA= new GpxListReader(fileA, AutoPause.NULL).getGpxList();
            GpxList listB=new GpxListReader(fileB, AutoPause.NULL).getGpxList();
            
            Foc fileCopy = getTestDirectory(getContext()).child("test_copy.gpx");
            GpxListWriter writer = new GpxListWriter(listA, fileCopy);
            writer.flushOutput();
            writer.close();
            
            GpxList listC=new GpxListReader(fileCopy, AutoPause.NULL).getGpxList();
            

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
