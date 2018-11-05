package ch.bailu.aat.description;

import android.content.Context;

import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;

public class TrackSizeDescription extends ContentDescription {
    private String value="";
    private int size=-1;

    public TrackSizeDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

        GpxList track =  info.getGpxList();
        if (track != null && size != track.getPointList().size()) {
            size = track.getPointList().size();

            value =   "P: " + track.getPointList().size() +
                    ", M: " + track.getMarkerList().size() +
                    ", S: " + track.getSegmentList().size();
        }
    }


    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_size);
    }
}
