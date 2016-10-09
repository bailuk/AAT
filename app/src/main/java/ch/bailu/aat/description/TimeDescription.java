package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class TimeDescription extends LongDescription {
    
    public TimeDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getString(R.string.time);
    }

    @Override
    public String getUnit() {
        return "";
    }

    @Override
    public String getValue() {
        StringBuilder builder = new StringBuilder();
        int seconds, hours, minutes;
        
        // 1. calculate milliseconds to unit
        seconds = (int) (getCache() / 1000);
        minutes = seconds / 60;
        hours = minutes / 60;
        
        // 2. cut away values that belong to a higher unit
        seconds -= minutes * 60;
        minutes -= hours * 60;

        appendValueAndDelimer(builder, hours);
        appendValueAndDelimer(builder, minutes);
        appendValue(builder, seconds); 
        return builder.toString();
        
    }

    private void appendValueAndDelimer(StringBuilder builder, int value) {
        appendValue(builder,value);
        builder.append(":");
    }
    
    private void appendValue(StringBuilder builder, int value) {
        if (value < 10) {
            builder.append("0");
        }
        builder.append(value);
    }
    
    @Override
    public void onContentUpdated(GpxInformation info) {
        setCache(info.getTimeDelta());
    }

    /*
    @Override
    public int getStrlen() {
        return 8;
    }
    */
}
