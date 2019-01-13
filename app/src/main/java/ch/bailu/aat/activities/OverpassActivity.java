package ch.bailu.aat.activities;

import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListEntry;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.OverpassApi;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.osm_features.MapFeaturesListView;
import ch.bailu.aat.views.osm_features.MapFeaturesView;

public class OverpassActivity extends AbsOsmApiActivity  {
    
    
    private View next;
    private MapFeaturesView osm_features;
    private MultiView multiView;


    @Override
    public View createMainContentView(MainControlBar bar) {
        if (AppLayout.isTablet(this)) {
            PercentageLayout mainView = new PercentageLayout(this);
            mainView.setOrientation(LinearLayout.HORIZONTAL);

            mainView.add(super.createMainContentView(bar),50);
            mainView.add(createOsmFeaturesView(bar), 50);

            return mainView;
        } else {

            multiView = new MultiView(this, OverpassActivity.class.getSimpleName());
            multiView.add(super.createMainContentView(bar));
            multiView.add(createOsmFeaturesView(bar));
            return multiView;
        }

    }

    private View createOsmFeaturesView(MainControlBar bar) {
        osm_features = new MapFeaturesView(getServiceContext());
        osm_features.setOnTextSelected((e, action, variant) -> {
            if (action == MapFeaturesListView.OnSelected.FILTER) {
                osm_features.setFilterText(e.summarySearchKey);

            } else if (action == MapFeaturesListView.OnSelected.EDIT){
                insertLine(variant);
                inputMultiView.setActive(0);
                if (multiView != null) multiView.setNext();

            } else if (action == MapFeaturesListView.OnSelected.SHOW) {

            }
        });

        AppTheme.alt.background(osm_features);
        //osm_features.setBackgroundColor(getAltBackgroundColor());

        return osm_features;
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        osm_features.onResume(getServiceContext());
    }


    @Override
    public void onPause() {
        osm_features.onPause(getServiceContext());
        super.onPause();
    }


    @Override
    public OsmApiHelper createUrlGenerator(BoundingBoxE6 boundingBox) throws SecurityException, IOException {
        return new OverpassApi(this,boundingBox);
    }


    @Override
    public void addButtons(ControlBar bar) {
        if (!AppLayout.isTablet(this)) {
            next = bar.addImageButton(R.drawable.go_next_inverse);
            ToolTip.set(next, R.string.tt_overpass_mapfeatures);
        } else {
            bar.addSpace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v== next) {
            if (multiView != null) multiView.setNext();
        } else {
            super.onClick(v);
        }
    }

}
