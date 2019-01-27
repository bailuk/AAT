package ch.bailu.aat.services.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.preferences.PresetDependent;
import ch.bailu.aat.preferences.location.SolidLocationProvider;
import ch.bailu.aat.preferences.presets.SolidPreset;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class LocationService extends VirtualService
        implements Closeable,  OnSharedPreferenceChangeListener, PresetDependent {

    private final SolidLocationProvider sprovider;

    private final ArrayList<LocationStackItem> itemList= new ArrayList<>();

    private LocationStackItem provider;
    private CleanLocation clean;
    private DirtyLocation dirty;
    private MissingTrigger missing;
    private AutopauseTrigger autopause;


    private int presetIndex;

    public LocationService(ServiceContext c) {
        super(c);

        sprovider = new SolidLocationProvider(c.getContext());
        sprovider.register(this);

        createLocationStack();
        createLocationProvider();

        setPresetIndex(new SolidPreset(c.getContext()).getIndex());
    }


    public void setPresetIndex(int i) {
        presetIndex = i;
        preferencesChanged(getContext(), SolidPreset.KEY, presetIndex);
    }


    private void createLocationStack() {
        clean = new CleanLocation();       
        itemList.add(clean);

        itemList.add(new DistanceFilter(lastItem()));

        autopause=new AutopauseTrigger(lastItem());
        itemList.add(autopause);

        missing = new MissingTrigger(lastItem()); 
        itemList.add(missing);

        itemList.add(new AccuracyFilter(lastItem()));
        itemList.add(new InformationFilter(lastItem()));


        dirty = new DirtyLocation(lastItem(), getContext());
        itemList.add(dirty);

        itemList.add(new NewAltitudeFromBarometer(lastItem(), getSContext()));
        itemList.add(new AdjustGpsAltitude(lastItem(), getContext()));

    }

    private LocationStackItem lastItem() {
        return itemList.get(itemList.size()-1);
    }

    private void createLocationProvider() {
        if (itemList.remove(provider)) {
            provider.close();
        }

        provider = sprovider.createProvider(lastItem());
        itemList.add(provider);
    }


    @Override
    public void close() {
        for (int i=0; i<itemList.size(); i++) 
            itemList.get(i).close();

        sprovider.unregister(this);
    }


    @Override
    public void preferencesChanged(Context c, String key, int presetIndex) {
        for (int i=0; i<itemList.size(); i++) 
            itemList.get(i).preferencesChanged(c, key, presetIndex);
    }

    public GpxInformation getLocationInformation() {
        return dirty.getLocationInformation();
    }


    public GpxPointInterface getCleanLocation() {
        return clean.getCleanLocation();
    }

    public boolean hasLoggableLocation() {
        return clean.hasLoggableLocation();
    }

    public boolean isAutopaused() {
        return autopause.isAutopaused();
    }

    public boolean isMissingUpdates() {
        return missing.isMissingUpdates();
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (sprovider.hasKey(key)) createLocationProvider();

        preferencesChanged(getContext(), key, presetIndex);
    }



    public void appendStatusText(StringBuilder builder) {
        builder.append("<h2>");
        builder.append(getClass().getSimpleName());
        builder.append("</h2><p>");


        for (int i=0; i<itemList.size(); i++) 
            itemList.get(i).appendStatusText(builder);

        builder.append("</p>");

    }
}
