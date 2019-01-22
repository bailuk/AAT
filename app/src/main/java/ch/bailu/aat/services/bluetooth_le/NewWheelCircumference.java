package ch.bailu.aat.services.bluetooth_le;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxDeltaHelper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;

public class NewWheelCircumference implements Closeable {

    private static final float MIN_SAMPLE_DISTANCE = 0.5f;
    private static final float MAX_SAMPLE_DISTANCE = 100f;
    private static final float MIN_ACCURACY = 10f;
    private static final int MIN_REVOLUTIONS = 10;
    private static final int MIN_SAMPLES = 10;

    private final ServiceContext scontext;
    private final Revolution revolution;

    private long revolutionsStart = 0;
    private long revolutionsDelta = 0;

    private GpxPointInterface previousLocation = null;

    private float distance = 0f;
    private int samples = 0;



    private final BroadcastReceiver onLocationChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            GpxInformation currentLocation = scontext.getLocationService().getLocationInformation();

            if (previousLocation != null && currentLocation.getAccuracy() <= MIN_ACCURACY) {

                final float dist = GpxDeltaHelper.getDistance(previousLocation, currentLocation);

                if (dist > MIN_SAMPLE_DISTANCE && dist < MAX_SAMPLE_DISTANCE) {
                    addSample(currentLocation, dist);

                } else {
                    reset(currentLocation);

                }

            } else {
                reset(currentLocation);
            }
        }
    };


    public NewWheelCircumference (ServiceContext sc, Revolution r) {
        scontext = sc;
        revolution = r;

        AppBroadcaster.register(sc.getContext(), onLocationChanged, AppBroadcaster.LOCATION_CHANGED);
    }


    private void addSample(GpxPointInterface location, float dist) {
        distance += dist;
        samples ++;
        revolutionsDelta = revolution.getTotalRevolutions() - revolutionsStart;
        previousLocation = location;
    }


    private void reset(GpxPointInterface location) {
        previousLocation = location;
        distance = 0f;
        samples = 0;
        revolutionsDelta = 0;
        revolutionsStart = revolution.getTotalRevolutions();

    }



    public float getCircumferenceSI() {
        if (samples >= MIN_SAMPLES  && revolutionsDelta >= MIN_REVOLUTIONS)
            return distance / revolutionsDelta;

        return 0f;
    }



    @Override
    public void close() {
        scontext.getContext().unregisterReceiver(onLocationChanged);
    }
}
