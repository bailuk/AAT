package ch.bailu.aat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.mapsforge.core.model.BoundingBox;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;

public class ActivitySwitcher {

    private final static int PHONE_CYCABLE = 3;
    private final static int TABLET_CYCABLE = 2;


    private static ArrayList<Entry> entries = null;
    private static int cycable=0;


    public static class Entry {
        public final String activityLabel;
        public final Class<?> activityClass;

        public Entry(String label, Class<?> c) {
            activityLabel=label;
            activityClass=c;
        }
        public void start(Context c) {
            ActivitySwitcher.start(c, activityClass);
        }
    }


    private final Activity callingActivity;

    public ActivitySwitcher(Activity acontext) {
        init(acontext);
        callingActivity = acontext;
    }

    private static void init(Context context) {
        if (entries==null) {
            entries = new ArrayList();

            if (AppLayout.isTablet(context)) {
                initTablet(context);
            } else {
                initPhone(context);
            }
            initBoth(context);
        }
    }

    private static void initTablet(Context c) {
        cycable = TABLET_CYCABLE;
        entries.add(new Entry(c.getString(R.string.intro_resume), CockpitTabletActivity.class));
    }


    private static void initPhone(Context c) {
        cycable = PHONE_CYCABLE;
        entries.add(new Entry(c.getString(R.string.intro_resume), CockpitActivity.class));
        entries.add(new Entry(c.getString(R.string.intro_cockpit2), CockpitSplitActivity.class));
    }

    private static void initBoth(Context c) {
        entries.add(new Entry(c.getString(R.string.intro_map), MapActivity.class));
        entries.add(new Entry(c.getString(R.string.intro_list), TrackListActivity.class));
        entries.add(new Entry(c.getString(R.string.intro_overlay_list), OverlayListActivity.class));
        entries.add(new Entry(c.getString(R.string.intro_external_list), ExternalListActivity.class));
        entries.add(new Entry(c.getString(R.string.intro_settings), PreferencesActivity.class));
        entries.add(new Entry(c.getString(R.string.intro_about) + " / " + c.getString(R.string.intro_readme), AboutActivity.class));

        if (BuildConfig.DEBUG)
            entries.add(new Entry(c.getString(R.string.intro_test), TestActivity.class));

    }
    public int size() {
        return entries.size();
    }


    public Entry get(int i) {
        return entries.get(i);
    }

    public void cycle() {
        for (int i=0; i<entries.size(); i++) {
            if (entries.get(i).activityClass.equals(callingActivity.getClass())) {
                int x = i+1;

                if (x > cycable-1) x = 0;

                entries.get(x).start(callingActivity);
                callingActivity.finish();
                break;
            }
        }
    }


    public static void start(Context context, Class<?> activityClass) {
        start(context, activityClass, activityClass.getSimpleName());
    }


    public static void start(Context context, Class<?> activityClass, String string) {
        Intent intent = new Intent();

        AppIntent.setFile(intent, string);
        start(context, activityClass, intent);
    }




    public static void start(Context context, Class<?> activityClass, File file) {
        Intent intent = new Intent();

        AppIntent.setFile(intent, file.getAbsolutePath());
        start(context, activityClass, intent);
    }




    public static void start(Context context, Class<?> activityClass, Intent intent) {
        intent.setClass(context, activityClass);
        intent.setAction(activityClass.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    public static void start(Context context, Class<?> a, BoundingBoxE6 box) {
        Intent intent = new Intent();
        AppIntent.setBoundingBox(intent, box);
        start(context, a, intent);

    }

    public static void start(Context context, Class<?> a, BoundingBox box) {
        Intent intent = new Intent();
        AppIntent.setBoundingBox(intent, box);
        start(context, a, intent);

    }
}
