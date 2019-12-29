package ch.bailu.aat.test;

import org.mapsforge.core.model.Tag;
import org.mapsforge.poi.storage.PointOfInterest;

import java.util.Set;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.GpxAttributesStatic;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class GpxPointPoi implements GpxPointInterface {

    private final static int KEY_ELE = Keys.toIndex("ele");
    private final static int KEY_ALTITUDE = Keys.toIndex("altitude");

    final PointOfInterest poi;
    final GpxAttributes attr;

    public GpxPointPoi(PointOfInterest p) {
        poi = p;
        attr = toAttributes(p.getTags());
    }

    @Override
    public double getAltitude() {
        if (attr.hasKey(KEY_ELE))
            return Double.valueOf(attr.get(KEY_ELE));

        if (attr.hasKey(KEY_ALTITUDE))
            return Double.valueOf(attr.get(KEY_ALTITUDE));

        return 0d;
    }

    private GpxAttributes toAttributes(Set<Tag> tags) {
        final GpxAttributesStatic.Tag[] newTags =
                new GpxAttributesStatic.Tag[tags.size()];

        int i=0;
        for(Tag tag : tags) {
            newTags[i++] =
                    new GpxAttributesStatic.Tag(Keys.toIndex(tag.key.replace(':', '_')), tag.value);
        }
        return new GpxAttributesStatic(newTags);
    }

    @Override
    public double getLongitude() {
        return poi.getLongitude();
    }

    @Override
    public double getLatitude() {
        return poi.getLatitude();
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public GpxAttributes getAttributes() {
        return attr;
    }

    @Override
    public int getLatitudeE6() {
        return (int) (getLatitude() * 1e6);
    }

    @Override
    public int getLongitudeE6() {
        return (int) (getLongitude() * 1e6);
    }
}
