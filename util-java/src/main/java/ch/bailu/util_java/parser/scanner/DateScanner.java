package ch.bailu.util_java.parser.scanner;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ch.bailu.util_java.io.Stream;


public class DateScanner extends AbsScanner {
    // Localtime to UTC fix
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private final TimeZone localTimeZone;
    private long localOffsetMillis;

    private final Calendar utcDate;
    private final IntegerScanner minute;
    private final IntegerScanner hour;
    private final DoubleScanner seconds;

    private long millis;
    private long utcDateMillis;
    private final int[] dateBuffer = new int[10];

    public DateScanner(long l) {

        millis = l;
        minute = new IntegerScanner();
        hour = new IntegerScanner();
        seconds = new DoubleScanner(3);

        utcDateMillis = 0;

        localTimeZone = TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID());

        utcDate = new GregorianCalendar();
    }

    @Override
    public void scan(Stream stream) throws IOException {
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
        if (stream.haveA('T')) scanTime(stream);

        if (stream.haveA('Z') == false) {
            millis -= localOffsetMillis;
        }
    }

    public long getTimeMillis() {
        return millis;
    }

    private void scanDate() {
        int[] list = new int[3];
        int x=0;

        for (int aDateBuffer : dateBuffer) {
            if (aDateBuffer == '-') {
                x++;
            } else {
                list[x] *= 10;
                list[x] += aDateBuffer - '0';
            }
        }

        utcDate.clear();
        utcDate.setTimeZone(UTC);
        utcDate.set(list[0],list[1]-1,list[2]); // year, month (zero-based), day

        utcDateMillis = utcDate.getTimeInMillis();
        millis = utcDateMillis;

        localOffsetMillis  = localTimeZone.getOffset(utcDateMillis);
    }


    private void scanTime(Stream stream) throws IOException {
        hour.scan(stream);
        minute.scan(stream);
        seconds.scan(stream);


        millis = seconds.getInt();
        millis += minute.getInteger()*60*1000;
        millis += hour.getInteger()*60*60*1000;

        millis += utcDateMillis;
    }

}
