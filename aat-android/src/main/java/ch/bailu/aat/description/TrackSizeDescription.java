package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.resources.Res;

public class TrackSizeDescription extends ContentDescription {
    private String value="";
    private int size=-1;

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
        return Res.str().d_size();
    }
}
