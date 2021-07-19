package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AltitudeDelta;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class AscendDescription extends AltitudeDescription {
    public AscendDescription(StorageInterface storage) {
        super(storage);
    }

    @Override
    public String getLabel() {
        return Res.str().d_ascend();
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getAsFloat(AltitudeDelta.INDEX_ASCEND));
    }
}
