package ch.bailu.aat.activities;

import android.content.Intent;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.description.DateDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.NameDescription;
import ch.bailu.aat_lib.description.TrackSizeDescription;
import ch.bailu.aat.preferences.system.SolidExternalDirectory;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.foc.Foc;

public class ExternalListActivity extends AbsGpxListActivity {

    @Override
    public ContentDescription[] getGpxListItemData() {
        return new ContentDescription[] {
                new DateDescription(),
                new DistanceDescription(getAppContext().getStorage()),
                new NameDescription()
        };
    }


    @Override
    public ContentDescription[] getSummaryData() {
        return new ContentDescription[] {

                new TrackSizeDescription(),
        };
    }

    @Override
    public void displayFile() {
        Intent intent=new Intent(this,FileContentActivity.class);
        startActivity(intent);
    }


    @Override
    public Foc getDirectory() {
        return new SolidExternalDirectory(this).getValueAsFile();
    }


    @Override
    public String getLabel() {
        return getString(R.string.intro_external_list);
    }

}
