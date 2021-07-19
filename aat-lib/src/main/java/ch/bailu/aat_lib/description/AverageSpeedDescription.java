package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class AverageSpeedDescription extends SpeedDescription {

    public AverageSpeedDescription(StorageInterface storage) {
        super(storage);
    }

    @Override
    public String getLabel() {
        return Res.str().average();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getSpeed());
    }
}
