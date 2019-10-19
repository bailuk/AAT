package ch.bailu.aat.description;

import ch.bailu.aat.gpx.GpxInformation;

public class ContentDescriptions extends ContentDescription {

    private final ContentDescription[] descriptions;

    public ContentDescriptions(ContentDescription... d) {
        super(d[0].getContext());
        descriptions = d;
    }

    @Override
    public String getValue() {
        String v="";
        String u="";
        String s="";
        for (ContentDescription d: descriptions) {

            v = v+ s + d.getValue();
            u = d.getUnit();
            if (u.length() > 0) v+= " " + u;
            s= ", ";
        }
        return v;
    }

    @Override
    public String getLabel() {
        String l="";
        String s="";
        for (ContentDescription d: descriptions) {
            l = l + s + d.getLabel();
            s= ", ";
        }
        return l;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        for (ContentDescription d: descriptions) {
            d.onContentUpdated(iid, info);
        }
    }
}
