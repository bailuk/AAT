package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;


public abstract class ContentDescription
        implements ContentInterface, OnContentUpdatedInterface {

    public static final String VALUE_DISABLED = "--";

    private final Context context;

    protected static final String NULL_STRING="";
    public ContentDescription(Context c) {
        context = c;
    }

    public Context getContext() {
        return context;
    }


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
