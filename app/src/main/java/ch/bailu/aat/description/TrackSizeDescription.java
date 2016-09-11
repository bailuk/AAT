package ch.bailu.aat.description;

import java.util.Locale;

import android.content.Context;
import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;

public class TrackSizeDescription extends ContentDescription {
    private String value="";
    private int size=-1;

    public TrackSizeDescription(Context c) {
        super(c);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {

        GpxList track =  info.getGpxList();
        if (track != null && size != track.getPointList().size()) {
            size = track.getPointList().size();

            value = String.format(Locale.getDefault(),"P: %d, M: %d, S: %d", 
                    track.getPointList().size(),
                    track.getMarkerList().size(),
                    track.getSegmentList().size());
        }
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return getString(R.string.d_size);
    }
}
