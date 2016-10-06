package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class SolidLocationProvider extends SolidStaticIndexList {

    private static final String KEY="location_provider";
    private static final String[]  LOCATION_PROVIDER_NAME = {
        "System GPS*", 
        "System GPS 2000ms*", 
        "System GPS 3000ms*", 
        "Mock Location*"};
    
    
    public SolidLocationProvider(Context c) {
        super(Storage.global(c), KEY,
                LOCATION_PROVIDER_NAME);
    }
    
        
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_location_provider); 
    }

    
}
