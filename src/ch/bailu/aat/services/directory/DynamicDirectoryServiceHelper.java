package ch.bailu.aat.services.directory;

import java.io.IOException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.preferences.SolidFilterFrom;
import ch.bailu.aat.preferences.SolidFilterTo;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTrackListFilter;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class DynamicDirectoryServiceHelper
                            extends DirectoryServiceHelper 
                            implements OnSharedPreferenceChangeListener{


    private final SolidTrackListFilter sfilter;
    private final SolidFilterFrom sfrom;
    private final SolidFilterTo sto;
    private final Storage storage;

    
    public DynamicDirectoryServiceHelper(ServiceContext sc) throws IOException {
        super(sc.getDirectoryService(),new SolidPreset(sc.getContext()).getDirectory());

        int preset = new SolidPreset(sc.getContext()).getIndex();

        sfilter = new SolidTrackListFilter(sc.getContext(), preset);
        sfrom = new SolidFilterFrom(sc.getContext(), preset);;
        sto = new SolidFilterTo(sc.getContext(), preset);;

        storage = Storage.preset(sc.getContext());
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
