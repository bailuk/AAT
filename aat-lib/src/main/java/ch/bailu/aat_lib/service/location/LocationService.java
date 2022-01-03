package ch.bailu.aat_lib.service.location;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.OnPresetPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.util.WithStatusText;

/**
 * Maintains a stack of location devices and filters. Will broadcast location changes.
 * Provides two locations:
 * - Clean: Loggable location with precision according to user settings
 * - Dirty: Latest available location update. Can be from network or saved from last app run.
 */
public final class LocationService extends VirtualService
        implements LocationServiceInterface, Closeable, OnPresetPreferencesChanged, WithStatusText, OnPreferencesChanged {

    public final static int INITIAL_STATE = StateID.WAIT;

    private final SolidLocationProvider sprovider;

    private final ArrayList<LocationStackItem> itemList= new ArrayList<>();

    private LocationStackItem provider;
    private CleanLocation clean;
    private DirtyLocation dirty;
    private MissingTrigger missing;
    private AutopauseTrigger autopause;


    private int presetIndex;

    public LocationService(SolidLocationProvider sprovider, Broadcaster broadcastInterface) {
        super();

        this.sprovider = sprovider;
        sprovider.register(this);

        createLocationStack(broadcastInterface);
        createLocationProvider();

        setPresetIndex(new SolidPreset(sprovider.getStorage()).getIndex());
    }


    public synchronized void setPresetIndex(int i) {
        presetIndex = i;
        onPreferencesChanged(sprovider.getStorage(),SolidPreset.KEY, presetIndex);
    }


    private void createLocationStack(Broadcaster broadcastInterface) {
        clean = new CleanLocation();
        itemList.add(clean);

        itemList.add(new DistanceFilter(lastItem()));

        autopause = new AutopauseTrigger(lastItem());
        itemList.add(autopause);

        missing = new MissingTrigger(lastItem());
        itemList.add(missing);

        itemList.add(new AccuracyFilter(lastItem()));
        itemList.add(new InformationFilter(lastItem()));


        dirty = new DirtyLocation(lastItem(), sprovider.getStorage(), broadcastInterface);
        itemList.add(dirty);


       // itemList.add(new NewAltitudeFromBarometer(lastItem(), getSContext()));
       // itemList.add(new AdjustGpsAltitude(lastItem(), getContext()));

    }

    private LocationStackItem lastItem() {
        return itemList.get(itemList.size()-1);
    }

    private void createLocationProvider() {
        if (itemList.remove(provider)) {
            provider.close();
        }
        provider = sprovider.createProvider(this, lastItem());
        itemList.add(provider);
    }


    @Override
    public synchronized void close() {
        for (int i=0; i<itemList.size(); i++)
            itemList.get(i).close();

        sprovider.unregister(this);
    }


    @Override
    public synchronized void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {
        for (int i=0; i<itemList.size(); i++)
            itemList.get(i).onPreferencesChanged(storage, key, presetIndex);
    }

    public synchronized GpxInformation getLocationInformation() {
        return dirty.getLocationInformation();
    }

    @Override
    public synchronized GpxInformation getLoggableLocationOrNull(GpxInformation old) {
        if (hasLoggableLocation(old)) {
            return getLoggableLocation();
        }
        return null;
    }

    private GpxInformation getLoggableLocation() {return clean.getLoggableLocation();}

    private boolean hasLoggableLocation(GpxInformation old) {
        return clean.hasLoggableLocation(old);
    }

    public synchronized boolean isAutopaused() {
        return autopause.isAutopaused();
    }

    public synchronized boolean isMissingUpdates() {
        return missing.isMissingUpdates();
    }


    @Override
    public synchronized void onPreferencesChanged(StorageInterface storage, String key) {
        if (sprovider.hasKey(key)) createLocationProvider();

        onPreferencesChanged(storage, key, presetIndex);
    }


    public synchronized void appendStatusText(StringBuilder builder) {
        for (int i=0; i<itemList.size(); i++)
            itemList.get(i).appendStatusText(builder);
    }
}
