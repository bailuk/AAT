package ch.bailu.aat.gpx.xml_parser.scanner;

import java.util.ArrayList;
import java.util.Collections;

import ch.bailu.aat.gpx.GpxAttributesStatic;

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
        list.add(new GpxAttributesStatic.Tag(k, v));
    }

    public GpxAttributesStatic get() {
        return new GpxAttributesStatic(list.toArray(new GpxAttributesStatic.Tag[]{}));
    }
}
