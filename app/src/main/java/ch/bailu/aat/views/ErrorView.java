package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.foc.Foc;


public class ErrorView extends TextView  {

    private final static int BACKGROUND = Color.rgb(50,0,0);
    private final static int PADDING = 20;




    private final BroadcastReceiver onMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ErrorView.this.displayError(intent);
        }
    };

    public ErrorView(Context c) {
        super(c);
        setTextColor(Color.RED);
        setBackgroundColor(BACKGROUND);
        setVisibility(GONE);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }



    private void displayError(Intent intent) {
        String message = intent.getStringExtra(AppLog.EXTRA_MESSAGE);
        displayError(message);
    }

    /**
     * Gets an object from the cache service and displays its exception
     * @param serviceContext needed to access cache service
     * @param file needed to identify the object from the service context
     */
    public void displayError(ServiceContext serviceContext, Foc file) {
        new InsideContext(serviceContext) {

            @Override
            public void run() {
                Obj obj = serviceContext.getCacheService().getObject(file.getPath());

                displayError(obj.getException());

                obj.free();
            }
        };
    }


    /**
     * Get an error message from an exception and display it as a message
     * @param exception that gets displayed
     */
    public void displayError(Exception exception) {
        displayError(exceptionToCharSequence(exception));
    }


    /**
     * Display an error message
     * @param error message that gets displayed
     */
    public void displayError(CharSequence error) {
        if (error == null || error.length() == 0) {
            clear();
        } else {
            setText(error);
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


    /**
     * Clear the error display
     */
    public void clear() {
        setText("");
        setVisibility(GONE);
    }


    public void attach() {
        AppBroadcaster.register(getContext(), onMessage, AppLog.LOG_E);
    }


    public void detach() {
        getContext().unregisterReceiver(onMessage);
    }
}
