package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.fs.foc.FocAndroid;

public class PathDescription extends NameDescription {
    
    public PathDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        updateName(FocAndroid.factory(getContext(), info.getPath()).getPathName());
    }
    
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_path);
    }
}
