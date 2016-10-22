package ch.bailu.aat.description;

import android.content.Context;
import android.content.ContextWrapper;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;


public abstract class ContentDescription extends ContextWrapper
        implements ContentInterface, OnContentUpdatedInterface {
    
    protected static final String NULL_STRING="";
    public ContentDescription(Context c) {
        super(c);
    }
    
    public abstract String getValue(); 
    public abstract String getLabel(); 
    
    public String getUnit() {
        return NULL_STRING;
    }



    @Override
    public String getValueAsString() {
        return getValue() + " " + getUnit();
    }
}
