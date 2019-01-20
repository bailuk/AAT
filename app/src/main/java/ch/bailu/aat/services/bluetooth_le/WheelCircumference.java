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

public class WheelCircumference implements Closeable {
    private static final float MIN_SAMPLE_DISTANCE = 0.5f;
    private static final float MAX_SAMPLE_DISTANCE = 100f;
    private static final float MIN_ACCURACY = 5f;
    private static final int MIN_REVOLUTIONS = 10;
    private static final int SAMPLE_COUNT = 10;

    private final ServiceContext scontext;
    private final Revolution revolution;
    private final Sample samples[] = new Sample[SAMPLE_COUNT];

    private float distance;
    private int revolutions;

    private int next = 0;

    private boolean registered = true;

    private final BroadcastReceiver onLocationChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            GpxInformation location = scontext.getLocationService().getLocationInformation();

            if (location.getAccuracy() <= MIN_ACCURACY) {
                samples[next] = new Sample(location, revolution.getTotalRevolutions());
                next = (next + 1) % SAMPLE_COUNT;
            }

        }
    };

    public WheelCircumference (ServiceContext sc, Revolution r) {
        scontext = sc;
        revolution = r;
        AppBroadcaster.register(sc.getContext(), onLocationChanged, AppBroadcaster.LOCATION_CHANGED);
    }


    public float getCircumference() {
        if (haveAllSamples() && haveMinRevolutions() && haveUsableDistances()) {
            return distance / revolutions;
        }

        return 0f;
    }


    private boolean haveAllSamples() {
        for (Sample s : samples) {
            if (s == null)
                return false;
        }

        return true;
    }


    private boolean haveUsableDistances() {
        int a = next;
        int b = a++;

        distance = 0f;

        for (int i = 0; i < SAMPLE_COUNT - 1; i++) {
            a = a % SAMPLE_COUNT;
            b = b % SAMPLE_COUNT;


            float d = GpxDeltaHelper.getDistance(samples[a].location, samples[b].location);

            if (d < MIN_SAMPLE_DISTANCE || d > MAX_SAMPLE_DISTANCE)
                return false;

            distance += d;
            a++;
            b++;
        }

        return true;
    }


    private boolean haveMinRevolutions() {
        revolutions = getRevolutions();

        return revolutions >= MIN_REVOLUTIONS;

    }


    private int getRevolutions() {
        int a = next;
        int z = next-1;

        if (z < 0) z = SAMPLE_COUNT-1;

        long first = samples[a].revolutions;
        long last = samples[z].revolutions;

        return (int) (last - first);
    }

    @Override
    public void close() {
        if (registered) {
            scontext.getContext().unregisterReceiver(onLocationChanged);
            registered = false;
        }
    }


    private static class Sample {
        public final GpxPointInterface location;
        public final long revolutions;

        public Sample(GpxPointInterface l, long r) {
            revolutions = r;
            location = l;
        }
    }
}
