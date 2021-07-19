package ch.bailu.aat_lib.description;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;


public class PauseDescription extends TimeDescription {

    @Override
    public String getLabel() {
        return Res.str().pause();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getPause());
    }
}
