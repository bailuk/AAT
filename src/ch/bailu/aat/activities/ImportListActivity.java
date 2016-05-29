package ch.bailu.aat.activities;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppFile;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.views.ControlBar;

public class ImportListActivity extends AbsGpxListActivity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AppFile.importFile(this, getIntent());
    }        

 
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
        return getString(R.string.intro_import_list);
    }

    @Override
    public DirectoryServiceHelper createDirectoryServiceHelper() throws IOException {

        File directory=AppDirectory.getDataDirectory(this, AppDirectory.DIR_IMPORT); 

        return new DirectoryServiceHelper(getServiceContext().getDirectoryService(), directory, "");
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
        Intent intent=new Intent(this,FileContentActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            createDirectoryServiceHelper().rescanDirectory();
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
