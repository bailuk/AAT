package ch.bailu.aat.activities;

import android.content.Intent;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.description.AverageSpeedDescription;
import ch.bailu.aat_lib.description.DateDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.MaximumSpeedDescription;
import ch.bailu.aat_lib.description.NameDescription;
import ch.bailu.aat_lib.description.TrackSizeDescription;
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
                new DistanceDescription(getAppContext().getStorage()),
                new AverageSpeedDescription(getAppContext().getStorage()),
                new TimeDescription(),
                new NameDescription()
        };
    }

    @Override
    public ContentDescription[] getSummaryData() {
        return new ContentDescription[] {

                new TrackSizeDescription(),
                new MaximumSpeedDescription(getAppContext().getStorage()),
                new DistanceDescription(getAppContext().getStorage()),
                new AverageSpeedDescription(getAppContext().getStorage()),
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
        return new SolidPreset(getAppContext().getStorage()).getDirectory(new AndroidSolidDataDirectory(this));
    }

    @Override
    public String getLabel() {
        return getString(R.string.intro_list);
    }

}
