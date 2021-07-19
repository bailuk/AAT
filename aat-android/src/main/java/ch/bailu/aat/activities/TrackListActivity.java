package ch.bailu.aat.activities;

import android.content.Intent;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.foc.Foc;

public class TrackListActivity extends AbsGpxListActivity {

    @Override
    public ContentDescription[] getGpxListItemData() {
        return new ContentDescription[] {
                new DateDescription(),
                new DistanceDescription(getStorage()),
                new AverageSpeedDescription(getStorage()),
                new TimeDescription(),
                new NameDescription(this)
        };
    }

    @Override
    public ContentDescription[] getSummaryData() {
        return new ContentDescription[] {

                new TrackSizeDescription(this),
                new MaximumSpeedDescription(getStorage()),
                new DistanceDescription(getStorage()),
                new AverageSpeedDescription(getStorage()),
                new TimeDescription()
        };
    }

    @Override
    public void displayFile() {
        Intent intent=new Intent(this,FileContentActivity.class);
        startActivity(intent);
    }


    @Override
    public Foc getDirectory() {
        return new SolidPreset(new AndroidSolidDataDirectory(this)).getDirectory();
    }

    @Override
    public String getLabel() {
        return getString(R.string.intro_list);
    }

}
