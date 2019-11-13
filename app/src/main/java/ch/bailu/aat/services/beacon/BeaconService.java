package ch.bailu.aat.services.beacon;

import java.io.IOException;
import java.net.SocketException;
import java.net.InetSocketAddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import beacon.BeaconClient;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.WithStatusText;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.location.LocationService;

/**
 * Integrate the #BeaconClient into AAT as a #VirtualService.
 */
public final class BeaconService extends VirtualService implements WithStatusText {
    private BeaconClient client;
    private GpxInformation oldLocation = GpxInformation.NULL;

    private AsyncTask<GpxInformation, Integer, Integer> submitTask;

    public BeaconService(ServiceContext sc) {
        super(sc);

        try {
            // TODO: no hard-coded IP address
            client = new BeaconClient(new InetSocketAddress("138.201.185.127", 5598), 42);
        } catch (SocketException e) {
            return;
        }

        AppBroadcaster.register(getContext(), onLocation,
                                AppBroadcaster.LOCATION_CHANGED);
    }

    class SubmitTask extends AsyncTask<GpxInformation, Integer, Integer> {
        @Override
        protected Integer doInBackground(GpxInformation... i) {
            try {
                client.sendFix(i[0].getLatitude(), i[0].getLongitude());
            } catch (IOException e) {
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            submitTask = null;
        }
    }

    public void close() {
        if (submitTask != null)
            submitTask.cancel(true);

        if (client != null)
            client.close();

        getContext().unregisterReceiver(onLocation);
    }

    private void submit(GpxInformation location) {
        if (client == null)
            return;

        if (submitTask != null) {
            submitTask.cancel(true);
            submitTask = null;
        }

        submitTask = new SubmitTask().execute(location);
    }

    private void submit() {
        final LocationService l = getSContext().getLocationService();
        if (l.hasLoggableLocation(oldLocation)) {
            oldLocation = l.getLoggableLocation();
            submit(oldLocation);
        }
    }

    private final BroadcastReceiver onLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            submit();
        }
    };

    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("<p>Beacon!</p>");
    }

    public GpxInformation getLoggerInformation() {
        return oldLocation;
    }
}
