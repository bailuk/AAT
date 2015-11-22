package ch.bailu.aat.services.tracker.location;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.preferences.PresetDependent;
import ch.bailu.aat.preferences.SolidLocationProvider;

public class LocationStack implements CleanUp,  OnSharedPreferenceChangeListener, PresetDependent{
    private final SolidLocationProvider sprovider;
    
    private ArrayList<LocationStackItem> itemList=new ArrayList<LocationStackItem>();
    
    private LocationStackItem provider;
    private CleanLocation clean;
    private DirtyLocation dirty;
    private MissingTrigger missing;
    private AutopauseTrigger autopause;
    
    private final Context context;
    
    public LocationStack(Context c) {
        context=c;

        sprovider = new SolidLocationProvider(c);
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
        itemList.add(new InformationFilter(lastItem()));
        
        dirty = new DirtyLocation(lastItem(),context); 
        itemList.add(dirty);
    }

    private LocationStackItem lastItem() {
        return itemList.get(itemList.size()-1);
    }
    
    private void createLocationProvider() {
        if (itemList.remove(provider)) {
            provider.cleanUp();
        }
        
        if      (sprovider.getIndex()==0) provider = new SystemLocation(lastItem(), context);
        else if (sprovider.getIndex()==1) provider = new SystemLocation(lastItem(), context, 2000);
        else if (sprovider.getIndex()==2) provider = new SystemLocation(lastItem(), context, 3000);
        else                              provider = new MockLocation(context, lastItem());
        
        itemList.add(provider);
    }


    @Override
    public void cleanUp() {
        for (int i=0; i<itemList.size(); i++) 
            itemList.get(i).cleanUp();
        
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
