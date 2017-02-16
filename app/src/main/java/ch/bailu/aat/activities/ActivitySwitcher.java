package ch.bailu.aat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.mapsforge.core.model.BoundingBox;

import java.io.File;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLayout;

public class ActivitySwitcher {

    private final static Entry PHONE_LIST[] = {
            new Entry(R.string.intro_resume, CockpitActivity.class),
            new Entry(R.string.intro_cockpit2, CockpitSplitActivity.class),
            new Entry(R.string.intro_map, MapActivity.class),
            new Entry(R.string.intro_list, TrackListActivity.class),
            new Entry(R.string.intro_overlay_list, OverlayListActivity.class),
            new Entry(R.string.intro_import_list, ImportListActivity.class),
            new Entry(R.string.intro_settings, PreferencesActivity.class),
            new Entry(R.string.intro_about, AboutActivity.class),
            new Entry(R.string.intro_test, TestActivity.class),
    };

    private final static int PHONE_CYCABLE = 3;


    private final static Entry TABLET_LIST[] = {
            new Entry(R.string.intro_resume, CockpitTabletActivity.class),
            new Entry(R.string.intro_map, MapActivity.class),
            new Entry(R.string.intro_list, TrackListActivity.class),
            new Entry(R.string.intro_overlay_list, OverlayListActivity.class),
            new Entry(R.string.intro_import_list, ImportListActivity.class),
            new Entry(R.string.intro_settings, PreferencesActivity.class),
            new Entry(R.string.intro_about, AboutActivity.class),
            new Entry(R.string.intro_test, TestActivity.class),
    };
    private final static int TABLET_CYCABLE = 2;


    private static int cycable=0;
    private static Entry[] entries=null;

    public static class Entry {
        public final int activityLabel;
        public final Class<?> activityClass;

        public Entry(int label, Class<?> c) {
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
            if (AppLayout.isTablet(context)) {
                entries = TABLET_LIST;
                cycable = TABLET_CYCABLE;
            } else {
                entries = PHONE_LIST;
                cycable = PHONE_CYCABLE;
            }
        }
    }

    public int getActivityCount() {
        if (BuildConfig.DEBUG) return entries.length;
        return entries.length-1;
    }


    public Entry getActivity(int i) {
        return entries[i];
    }

    public void cycle() {
        for (int i=0; i<entries.length; i++) {
            if (entries[i].activityClass.equals(callingActivity.getClass())) {
                if (i+1 > cycable-1) {
                    entries[0].start(callingActivity);
                } else {
                    entries[i+1].start(callingActivity);
                }
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
