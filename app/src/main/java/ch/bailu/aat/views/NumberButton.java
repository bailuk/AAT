package ch.bailu.aat.views;

import ch.bailu.aat.description.ContentDescription;

public class NumberButton extends NumberView {
    
    
    public NumberButton(ContentDescription data, int filter) {
        super(data, filter);
        setBackgroundResource(ch.bailu.aat.R.drawable.button);
        setPadding(0, 0, 0, 0);
        setFocusable(true);
    }

    
}
