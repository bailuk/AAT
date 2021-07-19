package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class AccuracyDescription extends AltitudeDescription {
    public AccuracyDescription(StorageInterface storageInterface) {
        super(storageInterface);
    }


    @Override
    public String getLabel() {
        return Res.str().d_accuracy();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAccuracy());
    }
}
