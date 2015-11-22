package ch.bailu.aat.activities;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.R;



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
    public DirectoryServiceHelper createDirectoryServiceHelper(
            DirectoryService service) throws IOException {

        File directory=AppDirectory.getDataDirectory(this, AppDirectory.DIR_OVERLAY); 

        return new DirectoryServiceHelper(service, directory, "");
    }
    

    @Override
    public void createSummaryView(LinearLayout layout,
            DirectoryService directory) {
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


    @Override
    public void onResume() {
        super.onResume();

        try {
            createDirectoryServiceHelper(getDirectoryService()).rescanDirectory();
        } catch (Exception e) {}

    }
    
    public void createLabel(ControlBar bar, String labelText) {
        TextView label = new TextView(this);
        
        label.setText(labelText);
        label.setTextColor(AppTheme.getHighlightColor());
        label.setTextSize(25);
        bar.addViewIgnoreSize(label);
        
    }



}
