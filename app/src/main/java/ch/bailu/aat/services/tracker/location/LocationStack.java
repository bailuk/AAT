package ch.bailu.aat.services.tracker.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.preferences.PresetDependent;
import ch.bailu.aat.preferences.SolidLocationProvider;
import ch.bailu.aat.services.ServiceContext;

public class LocationStack implements Closeable,  OnSharedPreferenceChangeListener, PresetDependent{
    private final SolidLocationProvider sprovider;

    private final ArrayList<LocationStackItem> itemList= new ArrayList<>();

    private LocationStackItem provider;
    private CleanLocation clean;
    private DirtyLocation dirty;
    private MissingTrigger missing;
    private AutopauseTrigger autopause;

    private final ServiceContext scontext;

    public LocationStack(ServiceContext c) {
        scontext=c;

        sprovider = new SolidLocationProvider(c.getContext());
        sprovider.register(this);

        createLocationStack();
        createLocationProvider();
    }



    private void createLocationStack() {
        clean = new CleanLocation();       
        itemList.add(clean);

        itemList.add(new DistanceFilter(lastItem()));

        autopause=new AutopauseTrigger(lastItem());
        itemList.add(autopause);

        missing = new MissingTrigger(lastItem()); 
        itemList.add(missing);

        itemList.add(new AccelerationFilter(lastItem()));
        itemList.add(new AccuracyFilter(lastItem()));
        //itemList.add(new InformationFilter(lastItem()));

        dirty = new DirtyLocation(lastItem(),scontext.getContext()); 
        itemList.add(dirty);
    }

    private LocationStackItem lastItem() {
        return itemList.get(itemList.size()-1);
    }

    private void createLocationProvider() {
        if (itemList.remove(provider)) {
            provider.close();
        }

        if      (sprovider.getIndex()==0) provider = new SystemLocation(lastItem(), scontext);
        else if (sprovider.getIndex()==1) provider = new SystemLocation(lastItem(), scontext, 2000);
        else if (sprovider.getIndex()==2) provider = new SystemLocation(lastItem(), scontext, 3000);
        else                              provider = new MockLocation(scontext.getContext(), lastItem());

        itemList.add(provider);
    }


    @Override
    public void close() {
        for (int i=0; i<itemList.size(); i++) 
            itemList.get(i).close();

        sprovider.unregister(this);
    }


    @Override
    public void preferencesChanged(Context c, int presetIndex) {
        for (int i=0; i<itemList.size(); i++) 
            itemList.get(i).preferencesChanged(c,presetIndex);
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
