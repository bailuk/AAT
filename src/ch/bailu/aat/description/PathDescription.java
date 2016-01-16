package ch.bailu.aat.description;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import android.content.Context;

public class PathDescription extends NameDescription {
    
    public PathDescription(Context context) {
        super(context);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        updateName(info.getPath());
    }
    
    
    @Override
    public String getLabel() {
        return getString(R.string.d_path);
    }
}
