package ch.bailu.aat.description;

import android.content.Context;
import android.content.ContextWrapper;


public abstract class ContentDescription extends ContextWrapper implements DescriptionInterface {
    
    protected static final String NULL_STRING="";
    public ContentDescription(Context c) {
        super(c);
    }
    
    public abstract String getValue(); 
    public abstract String getLabel(); 
    
    public String getUnit() {
        return NULL_STRING;
    }

    /*
    public int getStrlen() {
        return 4;
    }*/
}
