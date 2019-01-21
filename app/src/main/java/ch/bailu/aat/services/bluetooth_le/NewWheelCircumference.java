package ch.bailu.aat.services.bluetooth_le;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxDeltaHelper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;

public class NewWheelCircumference implements Closeable {

    private static final float MIN_SAMPLE_DISTANCE = 0.5f;
    private static final float MAX_SAMPLE_DISTANCE = 100f;
    private static final float MIN_ACCURACY = 5f;
    private static final int MIN_REVOLUTIONS = 10;
    private static final int MIN_SAMPLES = 10;

    private final ServiceContext scontext;
    private final Revolution revolution;

    private long revolutionsStart = 0;
    private long revolutionsDelta = 0;

    private GpxPointInterface locationStart = null;

    private float distance;
    private int samples = 0;



    private final BroadcastReceiver onLocationChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            GpxInformation location = scontext.getLocationService().getLocationInformation();

            if (locationStart == null) {
                reset(location);

            } else if (location.getAccuracy() <= MIN_ACCURACY) {

                float dist = GpxDeltaHelper.getDistance(locationStart, location);

                if (dist > MIN_SAMPLE_DISTANCE && dist < MAX_SAMPLE_DISTANCE) {
                    addSample(location, dist);

                } else {
                    reset(location);

                }

            } else {
                reset(location);
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
        samples++;
        revolutionsDelta = revolution.getTotalRevolutions() - revolutionsStart;
        locationStart = location;
    }


    private void reset(GpxPointInterface location) {
        locationStart = location;
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
