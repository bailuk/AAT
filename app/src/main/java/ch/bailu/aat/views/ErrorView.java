package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.util_java.foc.Foc;


public class ErrorView extends TextView  {

    private final static int BACKGROUND = Color.rgb(50,0,0);
    private final static int PADDING = 20;

    public ErrorView(Context c) {
        super(c);
        setTextColor(Color.RED);
        setBackgroundColor(BACKGROUND);
        setVisibility(GONE);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }


    public void displayError(ServiceContext sc, Foc file) {
        new InsideContext(sc) {

            @Override
            public void run() {
                ObjectHandle obj = sc.getCacheService().getObject(file.getPath());

                displayError(obj.getException());

                obj.free();
            }
        };
    }


    public void displayError(Exception e) {
        displayError(exceptionToCharSequence(e));
    }


    public void displayError(CharSequence e) {
        if (e == null || e.length() == 0) {
            clear();
        } else {
            setText(e);
            setVisibility(VISIBLE);
        }
    }


    private static CharSequence exceptionToCharSequence(Exception e) {
        if (e == null) {
            return null;
        } else if (e.getLocalizedMessage() != null) {
            return e.getLocalizedMessage();
        } else if (e.getMessage() != null) {
            return e.getMessage();
        } else {
            return e.getClass().getSimpleName();
        }
    }


    public void clear() {
        setText("");
        setVisibility(GONE);
    }
}
