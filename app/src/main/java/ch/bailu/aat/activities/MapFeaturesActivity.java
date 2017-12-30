package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.FeaturesList;
import ch.bailu.aat.views.bar.MainControlBar;


public class MapFeaturesActivity extends AbsDispatcher {
    
    private FeaturesList list;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ContentView contentView = new ContentView(this, ContentView.VERTICAL);
        

        final ControlBar bar = new MainControlBar(this, LinearLayout.HORIZONTAL, 6);
        
        bar.addIgnoreSize(AppTheme.getTitleTextView(this, R.string.query_features));

        list = new FeaturesList(this);

        contentView.addView(bar);
        contentView.addView(list);

        setContentView(contentView);

    }
    

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        loadList();
    }




    private void loadList() {
        new InsideContext(getServiceContext()) {
            @Override
            public void run() {
                list.loadList(
                        getServiceContext().getContext().getAssets(),
                        getServiceContext().getIconMapService());

            }
        };
    }

}
