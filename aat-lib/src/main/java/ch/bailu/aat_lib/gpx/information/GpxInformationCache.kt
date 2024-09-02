package ch.bailu.aat_lib.gpx.information;

import ch.bailu.aat_lib.dispatcher.TargetInterface;

public class GpxInformationCache {
    public GpxInformation info;
    public int infoID;


    public GpxInformationCache() {
        reset();
    }


    public void set(int _infoID, GpxInformation _info) {
        info = _info;
        infoID = _infoID;
    }


    public void reset() {
        set(InfoID.UNSPECIFIED, GpxInformation.NULL);
    }


    public void letUpdate(TargetInterface obj) {
        obj.onContentUpdated(infoID, info);
    }
}
