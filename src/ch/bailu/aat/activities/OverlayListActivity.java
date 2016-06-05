package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ControlBar;



public class OverlayListActivity extends AbsGpxListActivity {

    

    @Override
    public LinearLayout createHeader(LinearLayout contentView) {
        ControlBar header = new ControlBar(
                this, 
                AppLayout.getOrientationAlongSmallSide(this));

        
        createLabel(header,getLabelText());
        contentView.addView(header);
        
        

        return header;
    }

    
    public String getLabelText() {
        return getString(R.string.intro_overlay_list);
    }

    @Override
    public DirectoryServiceHelper createDirectoryServiceHelper() {
        File directory=AppDirectory.getDataDirectory(this, AppDirectory.DIR_OVERLAY); 

        return new DirectoryServiceHelper(getServiceContext(), directory);
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
        TextView label = new TextView(this);
        
        label.setText(labelText);
        label.setTextColor(AppTheme.getHighlightColor());
        label.setTextSize(25);
        bar.addViewIgnoreSize(label);
        
    }


 


}
