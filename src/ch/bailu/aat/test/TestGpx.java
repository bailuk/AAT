package ch.bailu.aat.test;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.parser.SimpleGpxListReader;
import ch.bailu.aat.gpx.writer.GpxListWriter;

public class TestGpx extends UnitTest {

 

    public TestGpx(Context c) {
        super(c);
    }


    @Override
    public void test() throws IOException, AssertionError {
        testFile(new File(getTestDirectory(getContext()),"test.gpx"), 2869, 1);
    }

    
    
    



    public void testFile(File file, int points, int segments) throws IOException, AssertionError {
            GpxList listA= new SimpleGpxListReader(file).getGpxList();

            File fileCopy = new File(getTestDirectory(getContext()),"test_copy.gpx");
            GpxListWriter writer = new GpxListWriter(listA, fileCopy);
            writer.flushOutput();
            writer.close();
            
            GpxList listB=new SimpleGpxListReader(fileCopy).getGpxList();

            assertEquals(listA.getPointList().size(), points);
            assertEquals(listA.getSegmentList().size(), segments);
            assertEquals(listA.getPointList().size(), listB.getPointList().size());
            assertEquals(listA.getSegmentList().size(), listB.getSegmentList().size());
            assertEquals(listA.getMarkerList().size(), listB.getMarkerList().size());
            
            assertEquals(listA.getDelta().getEndTime(), listB.getDelta().getEndTime());
            assertEquals(listA.getDelta().getStartTime(), listB.getDelta().getStartTime());
            assertEquals(listA.getDelta().getPause(), listB.getDelta().getPause());
            
            assertTrue( (segments == 1 || listA.getDelta().getPause()>0) );
            
            //fileCopy.deleteFile();
    }
    
}
