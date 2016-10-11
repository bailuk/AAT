package ch.bailu.aat.activities;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;

import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.helpers.file.FileAccess;
import ch.bailu.aat.views.FeaturesList;

public class MapFeatureListActivity extends AbsDispatcher {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private boolean contentViewSet = false;

    @Override
    public void onResumeWithService() {
        if (contentViewSet==false) {
            final Intent intent = getIntent();
            final String file = AppIntent.getFile(intent);

            FeaturesList list = new FeaturesList(getServiceContext(), new FileAccess(new File(file)));
            setContentView(list);

            contentViewSet=true;
        }

    }
    
}
