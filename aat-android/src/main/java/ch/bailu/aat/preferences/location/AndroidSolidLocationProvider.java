package ch.bailu.aat.preferences.location;

import android.content.Context;

import java.util.List;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.location.GpsLocation;
import ch.bailu.aat.services.location.GpsOrNetworkLocation;
import ch.bailu.aat.services.location.MockLocation;
import ch.bailu.aat.services.location.NetworkLocation;
import ch.bailu.aat.services.location.RealLocation;
import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.location.LocationStackItem;

public class AndroidSolidLocationProvider extends SolidLocationProvider {
    private static final String KEY = "location_provider";
    private static String[] provider_list = null;

    private static final int PRESETS = 6;


    private static String[] generateProviderList(Context c) {

        if (provider_list == null) {

            int size = PRESETS;
            List<String> providers = RealLocation.getAllLocationProvidersOrNull(c);

            if (providers != null) {
                size += providers.size();
            }

            provider_list = new String[size];
            provider_list[0] = c.getString(R.string.p_location_gps);
            provider_list[1] = c.getString(R.string.p_location_gps) + " 2000ms";
            provider_list[2] = c.getString(R.string.p_location_gps) + " 3000ms";
            provider_list[3] = c.getString(R.string.p_location_gpsnet);
            provider_list[4] = c.getString(R.string.p_location_network);
            provider_list[5] = c.getString(R.string.p_location_mock);

            if (providers != null) {
                int i = PRESETS;
                for (String provder : providers) {
                    provider_list[i] = provder;
                    i++;
                }
            }
        }
        return provider_list;
    }


    private final Context context;


    public Context getContext() {
        return context;
    }

    public AndroidSolidLocationProvider(Context c) {
        super(new Storage(c), generateProviderList(c));
        context = c;
    }


    @Override
    public LocationStackItem createProvider(LocationServiceInterface locationService, LocationStackItem last) {
        int i = getIndex();

        if (i == 1) return new GpsLocation(last, getContext(), 2000);
        else if (i == 2) return new GpsLocation(last, getContext(), 3000);
        else if (i == 3) return new GpsOrNetworkLocation(last, getContext(), 1000);
        else if (i == 4) return new NetworkLocation(last, getContext(), 5000);
        else if (i == 5) return new MockLocation(getContext(), last);
        else if (i >= PRESETS && i < provider_list.length) {
            return new RealLocation(last, getContext(), provider_list[i],
                    1000);
        } else return new GpsLocation(last, getContext(), 1000);
    }


    @Override
    public String getToolTip() {
        if (AppPermission.checkLocation(getContext()) == false) {
            return Res.str().p_location_provider_permission();
        }
        return super.getToolTip();
    }

}
