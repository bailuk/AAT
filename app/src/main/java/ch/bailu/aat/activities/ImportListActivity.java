package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import ch.bailu.aat.R;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.helpers.AppDirectory;

public class ImportListActivity extends AbsGpxListActivity {

    @Override
    public ContentDescription[] getGpxListItemData() {
        return new ContentDescription[] {
                new DateDescription(this),
                new DistanceDescription(this),
                new NameDescription(this)
        };          
    }

    
    @Override
    public ContentDescription[] getSummaryData() {
        return new ContentDescription[] {
                new NameDescription(this),
                new PathDescription(this),
                new TrackSizeDescription(this),
        };
    }
 
    @Override
    public void displayFile() {
        Intent intent=new Intent(this,FileContentActivity.class);
        startActivity(intent);
    }


    @Override
    public File getDirectory() {
        return AppDirectory.getDataDirectory(this, AppDirectory.DIR_IMPORT); 
    }


    @Override
    public String getLabel() {
        return getString(R.string.intro_import_list);
    }


   
}
