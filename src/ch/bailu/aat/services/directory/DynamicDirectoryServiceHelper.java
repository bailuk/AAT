package ch.bailu.aat.services.directory;

import java.io.IOException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import ch.bailu.aat.preferences.SolidFilterFrom;
import ch.bailu.aat.preferences.SolidFilterTo;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTrackListFilter;
import ch.bailu.aat.preferences.Storage;

public class DynamicDirectoryServiceHelper
                            extends DirectoryServiceHelper 
                            implements OnSharedPreferenceChangeListener{


    private final SolidTrackListFilter sfilter;
    private final SolidFilterFrom sfrom;
    private final SolidFilterTo sto;
    private final Storage storage;

    
    public DynamicDirectoryServiceHelper(DirectoryService s) throws IOException {
        super(s,new SolidPreset(s).getDirectory());

        int preset = new SolidPreset(s).getIndex();

        sfilter = new SolidTrackListFilter(s, preset);
        sfrom = new SolidFilterFrom(s, preset);;
        sto = new SolidFilterTo(s, preset);;

        storage = Storage.preset(s);
        storage.register(this);

        setSelection(createSelectionString());
        rescanDirectory();

    }


    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {

        if (sfilter.hasKey(key) || sfrom.hasKey(key) || sto.hasKey(key)) {
            setSelection(createSelectionString());
        }
    }

    private String createSelectionString() {
        String selection="";


        if (sfilter.getIndex()==1) {
            selection = GpxDbConstants.KEY_START_TIME 
                    + " BETWEEN " 
                    + String.valueOf(Math.min(sfrom.getValue(), sto.getValue())) 
                    + " AND " 
                    + String.valueOf(Math.max(sfrom.getValue(), sto.getValue())) ;
        }
        return selection;
    }



    @Override
    public void close() {
        super.close();
        storage.unregister(this);
    }
}
