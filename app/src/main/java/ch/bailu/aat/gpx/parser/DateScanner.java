package ch.bailu.aat.gpx.parser;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DateScanner {
    private final IntegerScanner minute;
    private final IntegerScanner hour;
    private final DoubleParser seconds;
    
    private long millis;
    private long dateBase;
    private int dateBuffer[] = new int[10];

    private final SimpleStream stream;
    
    public DateScanner(SimpleStream s, long l) {
        stream=s;
        
        millis=l;
        minute=new IntegerScanner(s);
        hour=new IntegerScanner(s);
        seconds = new DoubleParser(s,3);
        
        dateBase=0;
        
        dateBuffer= new int[10];
        for (int i=0; i<dateBuffer.length; i++) dateBuffer[i]=0;
    }
    
    public void parse() throws IOException {
        boolean dateNeedsRescan=false;
        
        stream.read();
        stream.skipWhitespace();
        
        for (int i=0; i < dateBuffer.length; i++) {
            if (dateBuffer[i] != stream.get()) {
                    dateBuffer[i]=stream.get();
                    dateNeedsRescan=true;
            }
            stream.read();
        }
        
        if (dateNeedsRescan) scanDate();
        if (stream.haveA('T')) scanTime();
        
    }
    
    public long getTimeMillis() {
        return millis;
    }
    
    private void scanDate() {
        Calendar date;
        
        
        int list[]=new int[3];
        int x=0;

        for (int aDateBuffer : dateBuffer) {
            if (aDateBuffer == '-') {
                x++;
            } else {
                list[x] *= 10;
                list[x] += aDateBuffer - '0';
            }
        }
        
        date = new GregorianCalendar(list[0],list[1]-1,list[2]); // year, month (zero-based), day
        dateBase = date.getTimeInMillis();
        millis=dateBase;
    }
    
    private void scanTime() throws IOException {
        
        //stream.read();
        hour.scan();
        //stream.read();
        minute.scan();
        //stream.read();
        seconds.scan();
        
                
        millis=seconds.getInt();
        millis+=minute.getInteger()*60*1000;
        millis+=hour.getInteger()*60*60*1000;
        
        millis+=dateBase;
    }
    
    

}
