package ch.bailu.aat.activities;

import android.content.Intent;

import java.io.File;

import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.helpers.file.FileAccess;
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
                    new FileAccess(new File(file)),
                    getServiceContext().getIconMapService() );
            setContentView(list);

            contentViewSet=true;
        }
    }

}
