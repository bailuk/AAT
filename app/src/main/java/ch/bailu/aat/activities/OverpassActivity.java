package ch.bailu.aat.activities;

import android.view.View;

import org.osmdroid.util.BoundingBoxOsm;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.OverpassApi;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.ControlBar;

public class OverpassActivity extends AbsOsmApiActivity  {
    
    
    private View  feature;

    

    @Override
    public void onClick(View v) {
        if (v==feature) {
            ActivitySwitcher.start(this, MapFeaturesActivity.class);
        } else {
            super.onClick(v);
        }
        
        
    }



    @Override
    public OsmApiHelper createUrlGenerator(BoundingBoxOsm boundingBox) throws SecurityException, IOException {
        return new OverpassApi(this,boundingBox);
    }



    @Override
    public void addButtons(ControlBar bar) {
        feature = bar.addImageButton(R.drawable.content_loading_inverse);
        ToolTip.set(feature, R.string.tt_overpass_mapfeatures);
    }

}
