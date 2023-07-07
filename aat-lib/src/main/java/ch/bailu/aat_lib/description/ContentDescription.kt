package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;

public abstract class ContentDescription
        implements ContentInterface, OnContentUpdatedInterface {

    public static final String VALUE_DISABLED = "--";

    protected static final String NULL_STRING="";

    public abstract String getValue();
    public abstract String getLabel();
    public String getLabelShort() {
        return getLabel();
    }

    public String getUnit() {
        return NULL_STRING;
    }


    @Override
    public String getValueAsString() {
        return getValue() + " " + getUnit();
    }

}
