package ch.bailu.aat_lib.description;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class MaximumSpeedDescription  extends SpeedDescription {

    public MaximumSpeedDescription(StorageInterface storage) {
        super(storage);
    }

    @Override
    public String getLabel() {
        return Res.str().maximum();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getAsFloat(MaxSpeed.INDEX_MAX_SPEED));
    }

}

