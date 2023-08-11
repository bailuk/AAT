package ch.bailu.aat_lib.xml.parser.scanner;

import java.util.ArrayList;
import java.util.Collections;

import ch.bailu.aat_lib.gpx.attributes.GpxAttributesStatic;
import ch.bailu.aat_lib.gpx.attributes.Keys;

public class Tags {
    public final ArrayList<GpxAttributesStatic.Tag> list = new ArrayList<>();

    public void clear() {
        list.clear();
    }

    public boolean notEmpty() {
        return list.size() > 0;
    }


    public void sort() {
        Collections.sort(list);
    }

    public void add(String k, String v) {
        list.add(new GpxAttributesStatic.Tag(Keys.toIndex(k), v));
    }

    public GpxAttributesStatic get() {
        return new GpxAttributesStatic(list.toArray(new GpxAttributesStatic.Tag[]{}));
    }
}
