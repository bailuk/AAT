package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AltitudeDelta;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class DescendDescription extends AltitudeDescription {
    public DescendDescription(StorageInterface storageInterface) {
        super(storageInterface);
    }

    @Override
    public String getLabel() {
        return Res.str().d_descend();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getAsFloat(AltitudeDelta.INDEX_DESCEND));
    }
}
