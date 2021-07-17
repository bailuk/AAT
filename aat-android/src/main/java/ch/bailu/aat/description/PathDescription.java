package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class PathDescription extends NameDescription {

    public PathDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        updateName(info.getFile().getPathName());
    }


    @Override
    public String getLabel() {
        return Res.str().d_path();
    }
}
