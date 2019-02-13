package ch.bailu.aat.gpx.attributes;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.preferences.SolidAutopause;

public class GpxListAttributes extends GpxSubAttributes {

    public static final GpxListAttributes NULL = new GpxListAttributes();

    private final GpxSubAttributes[] attributes;


    public GpxListAttributes(GpxSubAttributes... attr) {
        super(keysFromSubAttributes(attr));
        attributes = attr;
    }


    private static Keys keysFromSubAttributes(GpxSubAttributes[] attr) {
        final Keys keys = new Keys();

        for (GpxSubAttributes a : attr) {
            for (int i = 0; i< a.size(); i++) {
                keys.add(a.getKeyAt(i));
            }
        }
        return keys;
    }


    public static GpxListAttributes factoryTrackList() {
        return new GpxListAttributes(new MaxSpeed.Raw2());
    }


    public static GpxListAttributes factoryTrack(SolidAutopause spause) {
        return factoryTrack(new AutoPause.Time(
                        spause.getTriggerSpeed(),
                        spause.getTriggerLevelMillis()));
    }


    public static GpxListAttributes factoryTrack(AutoPause apause) {
        return new GpxListAttributes(new MaxSpeed.Samples(),
                apause,
                new AltitudeDelta.LastAverage(),
                new SampleRate.Cadence(), new SampleRate.HeartRate(), new Steps());
    }

    public static GpxListAttributes factoryRoute() {
        return new GpxListAttributes(new AltitudeDelta.LastAverage());
    }


    @Override
    public String get(int key) {
        for (GpxSubAttributes attr : attributes) {
            if (attr.hasKey(key)) return attr.get(key);
        }
        return NULL_VALUE;
    }


    @Override
    public float getAsFloat(int key) {
        for (GpxSubAttributes attr : attributes) {
            if (attr.hasKey(key)) return attr.getAsFloat(key);
        }
        return super.getAsFloat(key);
    }


    @Override
    public long getAsLong(int key) {
        for (GpxSubAttributes attr : attributes) {
            if (attr.hasKey(key)) return attr.getAsLong(key);
        }
        return super.getAsLong(key);
    }


    @Override
    public int getAsInteger(int key) {
        for (GpxSubAttributes attr : attributes) {
            if (attr.hasKey(key)) return attr.getAsInteger(key);
        }

        return super.getAsInteger(key);
    }


    public void update(GpxPointNode p) {
        update(p, false);
    }


    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {

        for (GpxSubAttributes attr : attributes) {
            autoPause = autoPause || attr.update(p, autoPause);
        }

        return autoPause;
    }
}
