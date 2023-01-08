package ch.bailu.aat_lib.description;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;

public class ContentDescriptions extends ContentDescription {

    private final ContentDescription[] descriptions;

    public ContentDescriptions(ContentDescription... d) {
        descriptions = d;
    }

    @Override
    public String getValue() {
        final StringBuilder value = new StringBuilder();
        String del = "";

        for (ContentDescription d: descriptions) {

            value.append(del).append(d.getValue());
            final String unit = d.getUnit();
            if (unit.length() > 0) value.append(" ").append(unit);
            del = ", ";
        }
        return value.toString();
    }

    @Override
    public String getLabel() {
        final StringBuilder label = new StringBuilder();
        String del = "";
        for (ContentDescription d: descriptions) {
            label.append(del).append(d.getLabel());
            del = ", ";
        }
        return label.toString();
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        for (ContentDescription d: descriptions) {
            d.onContentUpdated(iid, info);
        }
    }
}
