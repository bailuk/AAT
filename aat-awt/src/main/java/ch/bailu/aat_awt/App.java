package ch.bailu.aat_awt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.bailu.aat_awt.dispatcher.AwtBroadcaster;
import ch.bailu.aat_awt.logger.SL4JLogger;
import ch.bailu.aat_awt.preferences.AppPreferences;
import ch.bailu.aat_awt.preferences.SolidAwtFactory;
import ch.bailu.aat_awt.services.AwtServices;
import ch.bailu.aat_awt.window.AwtMainWindow;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.logger.BroadcastLogger;
import ch.bailu.aat_lib.preferences.SolidFactory;

public class App {


    private final AwtMainWindow window;
    private final AwtServices services;
    private final AwtBroadcaster broadcaster;

    private App() {

        broadcaster = new AwtBroadcaster();

        AppLog.set(new SL4JLogger());
        AppLog.setError(new BroadcastLogger(broadcaster, AppBroadcaster.LOG_ERROR, new SL4JLogger()));
        AppLog.setInfo(new BroadcastLogger(broadcaster, AppBroadcaster.LOG_INFO, new SL4JLogger()));

        SolidFactory sfactory = new SolidAwtFactory();

        services = new AwtServices(sfactory, broadcaster);

        window = new AwtMainWindow(getMapFiles(), broadcaster);
        window.onContentUpdated(InfoID.LOCATION, services.getLocationService().getLocationInformation());
        window.onContentUpdated(InfoID.TRACKER, services.getLocationService().getLocationInformation());


        broadcaster.register(data -> window.onContentUpdated(InfoID.LOCATION, services.getLocationService().getLocationInformation()), AppBroadcaster.LOCATION_CHANGED);
        broadcaster.register(data -> window.onContentUpdated(InfoID.TRACKER, services.getTrackerService().getLoggerInformation()), AppBroadcaster.TRACKER);

        services.getTrackerService().getState().onStartStop();
    }

    private static List<File> getMapFiles() {
        List<File> result = new ArrayList<File>(2);
        String home = System.getProperty("user.home");

        result.add(new File(home+"/maps/Alps/Alps_oam.osm.map"));
        return result;
    }



    private static App app;

    public static void main(String[] args) {
        try {
            app = new App();

        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(e);
            exit(1);
        }
    }

    public static void exit(int i) {
        if (app != null) {
            app.close();
        }
        AppPreferences.save();
        System.exit(i);
    }

    private void close() {
        services.getTrackerService().getState().onStartStop();
    }
}
