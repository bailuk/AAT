package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class SolidLocationProvider extends SolidStaticIndexList {

    private static final String KEY="location_provider";
    private static String[]  provider_list = null;


    private static String[] generateProviderList(Context c) {
        if (provider_list == null) {
            provider_list = new String[]{
                    c.getString(R.string.p_location_gps),
                    c.getString(R.string.p_location_gps) + " 2000ms",
                    c.getString(R.string.p_location_gps) + " 3000ms",
                    c.getString(R.string.p_location_mock)
            };
        }
        return provider_list;
    }

    public SolidLocationProvider(Context c) {
        super(Storage.global(c), KEY,
                generateProviderList(c));
    }
    
        
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_location_provider); 
    }

    
}
