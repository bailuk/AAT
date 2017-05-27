package ch.bailu.aat.activities;

import android.content.Intent;
import android.content.res.AssetManager;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.fs.foc.FocAsset;
import ch.bailu.aat.views.FeaturesList;

public class MapFeatureListActivity extends AbsDispatcher {
     private boolean contentViewSet = false;

    @Override
    public void onResumeWithService() {
        if (contentViewSet==false) {
            final Intent intent = getIntent();
            final String file = AppIntent.getFile(intent);

            final FeaturesList list = new FeaturesList(this);

            list.loadList(
                    new FocAsset(this.getAssets(), file),
                    getServiceContext().getIconMapService() );
            setContentView(list);

            contentViewSet=true;
        }
    }



}
