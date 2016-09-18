package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.helpers.file.FileAccess;
import ch.bailu.aat.views.FeaturesList;

public class MapFeatureListActivity extends AbsDispatcher {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    

    @Override
    public void onServicesUp(boolean firstRun) {
        final Intent intent = getIntent();
        final String file = AppIntent.getFile(intent);

        FeaturesList list = new FeaturesList(getServiceContext(), new FileAccess(new File(file)));
        setContentView(list);

    }
    
}
