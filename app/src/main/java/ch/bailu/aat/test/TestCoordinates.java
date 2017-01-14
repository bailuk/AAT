package ch.bailu.aat.test;

import android.content.Context;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.coordinates.UTMCoordinates;

public class TestCoordinates extends UnitTest {
  
    public TestCoordinates(Context c) {
        super(c);
    }
    

    public void test()  throws AssertionError {
       testUTM();
       testCH1903();
    }
    
    
    public void testUTM()  throws AssertionError {
        testUTM(48.024089, 7.789636, 5319686, 409755, 32);
        testUTM(-34.168281d,18.424966d, 6216188, 262641, 34);
        testUTM(51.624837d,-3.960800d, 5719750, 433491, 30);
        testUTM(47.617273d, -122.262268d, 5274027, 555437, 10);
    }
    
    
    private void testUTM(double la, double lo, int n, int e , int z) throws AssertionError {
        UTMCoordinates utm = new UTMCoordinates(la,lo);

        assertEquals(z, utm.getZone());
        assertEquals(n, utm.getNorthing());
        assertEquals(e, utm.getEasting());
        assertEquals((la <0), utm.isInSouthernHemnisphere());
        
        LatLongE6 p=utm.toLatLongE6();
        assertEquals((int)la*1000, (int)(p.getLatitudeE6()/1e6)*1000);
        assertEquals((int)lo*1000, (int)(p.getLongitudeE6()/1e6)*1000);
        
        

    }
    
    public  void testCH1903()  throws AssertionError {
        int n=CH1903Coordinates.BERNE_SIX;
        int e=CH1903Coordinates.BERNE_SIY;
        
        CH1903Coordinates ch1 = new CH1903Coordinates(e,n);
        CH1903Coordinates ch2 = new CH1903Coordinates(ch1.toLatLongE6());

        assertEquals(n, ch2.getNorthing());
        assertEquals(e, ch2.getEasting());

        /*
        CH1903Coordinates ch4 = new CH1903Coordinates(Sexagesimal.toDecimalDegree(46, 2, 38.87f), 
                                                    Sexagesimal.toDecimalDegree(8,43, 49.79f));
        
        
        assertEquals(700000, ch4.getEasting());
        assertEquals(100000, ch4.getNorthing());
        */
        
        //GeoPoint p = ch4.toLatLongE6();
        //assertEquals((int)Math.round(Sexagesimal.toDecimalDegree(46, 2, 38.87f)*1e6d), p.getLatitudeE6());
        
    }
}
