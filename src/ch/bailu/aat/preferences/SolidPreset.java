package ch.bailu.aat.preferences;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.R;
import android.content.Context;

public class SolidPreset extends SolidStaticIndexList {
    final private static String KEY="preset";
    final private static String[] NAMES={"A0", "A1", "A2", "A3", "A4"};
   
    
    public SolidPreset(Context c) {
        super(Storage.global(c), KEY, NAMES);
    }

    
    public String getString() {
        SolidMET smet = new SolidMET(getContext(), getIndex());
        return smet.getString();
    }
    
    @Override
    public String[] getStringArray() {
        
        String[] array = new String[length()];
        
        for (int i=0; i< array.length; i++) {
            SolidMET smet = new SolidMET(getContext(), i);
            array[i] = smet.getString(); 
        }
        return array;
    }
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_preset);
    }
    
    
    public File getDirectory() throws IOException {
    	return AppDirectory.getTrackListDirectory(getContext(),getIndex());
    }
    
    
    public String getDirectoryName() throws IOException {
    	return getDirectory().toString();
    }
    
    public String getCacheDbName() throws IOException {
    	return AppDirectory.getTrackListCacheDb(getContext(),getIndex()).toString();
    }
    
    
}
