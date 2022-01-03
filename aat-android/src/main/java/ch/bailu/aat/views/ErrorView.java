package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.cache.CacheService;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
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
        String message = intent.getStringExtra(AppIntent.EXTRA_MESSAGE);
        displayError(message);
    }


    /**
     * Gets an {@link Obj} from the {@link CacheService}
     * and displays its exception if it has one
     * @param serviceContext needed to access  {@link CacheService}
     * @param file needed to identify the {@link Obj}
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
     * Get an error message from an exception and display it
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


    /**
     * Receive and display error logs sent from {@link AppLog}
      */
    public void registerReceiver() {
        OldAppBroadcaster.register(getContext(), onMessage, AppBroadcaster.LOG_ERROR);
    }


    /**
     * Stop displaying error logs sent from {@link AppLog}
     */
    public void unregisterReceiver() {
        getContext().unregisterReceiver(onMessage);
    }
}
