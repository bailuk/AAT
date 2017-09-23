package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.services.location.GpsLocation;
import ch.bailu.aat.services.location.GpsOrNetworkLocation;
import ch.bailu.aat.services.location.LocationStackItem;
import ch.bailu.aat.services.location.MockLocation;
import ch.bailu.aat.services.location.NetworkLocation;

public class SolidLocationProvider extends SolidStaticIndexList {

    private static final String KEY="location_provider";
    private static String[]  provider_list = null;


    private static String[] generateProviderList(Context c) {
        if (provider_list == null) {
            provider_list = new String[]{
                    c.getString(R.string.p_location_gps),
                    c.getString(R.string.p_location_gps) + " 2000ms",
                    c.getString(R.string.p_location_gps) + " 3000ms",
                    c.getString(R.string.p_location_gps) + " or Network*",
                    "Network only*",
                    c.getString(R.string.p_location_mock)
            };
        }
        return provider_list;
    }


    public SolidLocationProvider(Context c) {
        super(Storage.global(c), KEY,
                generateProviderList(c));
    }
    


    public LocationStackItem createProvider(LocationStackItem last) {
        int i = getIndex();

        if      (i==0) return new GpsLocation(last, getContext(), 1000);
        else if (i==1) return new GpsLocation(last, getContext(), 2000);
        else if (i==2) return new GpsLocation(last, getContext(), 3000);
        else if (i==3) return new GpsOrNetworkLocation(last, getContext(), 1000);
        else if (i==4) return new NetworkLocation(last, getContext(), 5000);
        else           return new MockLocation(getContext(), last);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_location_provider); 
    }

}
