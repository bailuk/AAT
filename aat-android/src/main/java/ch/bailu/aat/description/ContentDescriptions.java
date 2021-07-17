package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class ContentDescriptions extends ContentDescription {

    private final ContentDescription[] descriptions;

    public ContentDescriptions(ContentDescription... d) {
        descriptions = d;
    }

    @Override
    public String getValue() {
        StringBuilder v= new StringBuilder();
        String u="";
        String s="";
        for (ContentDescription d: descriptions) {

            v.append(s).append(d.getValue());
            u = d.getUnit();
            if (u.length() > 0) v.append(" ").append(u);
            s= ", ";
        }
        return v.toString();
    }

    @Override
    public String getLabel() {
        StringBuilder l= new StringBuilder();
        String s="";
        for (ContentDescription d: descriptions) {
            l.append(s).append(d.getLabel());
            s= ", ";
        }
        return l.toString();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        for (ContentDescription d: descriptions) {
            d.onContentUpdated(iid, info);
        }
    }
}
