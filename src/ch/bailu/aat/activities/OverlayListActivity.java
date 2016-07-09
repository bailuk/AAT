package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import android.widget.LinearLayout;
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ControlBar;



public class OverlayListActivity extends AbsGpxListActivity {

    

    @Override
    public void createHeader(ControlBar bar) {
        createLabel(bar,getLabelText());
    }

    
    public String getLabelText() {
        return getString(R.string.intro_overlay_list);
    }

    @Override
    public DirectoryServiceHelper createDirectoryServiceHelper() {
        File directory=AppDirectory.getDataDirectory(this, AppDirectory.DIR_OVERLAY); 

        return new DirectoryServiceHelper(this, directory);
    }
    

    @Override
    public void createSummaryView(LinearLayout layout) {
    }


    @Override
    public ContentDescription[] getGpxListItemData() {
        return new ContentDescription[] {
                new DateDescription(this),
                new DistanceDescription(this),
                new NameDescription(this)
        };    		
    }

    @Override
    public void displayFile() {
        Intent intent=new Intent(this,GpxEditorActivity.class);
        startActivity(intent);
    }


    public void createLabel(ControlBar bar, String labelText) {
        bar.addViewIgnoreSize(AppTheme.getTitleTextView(this, labelText));
        
    }


 


}
