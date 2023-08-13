package ch.bailu.aat.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import ch.bailu.aat.R
import ch.bailu.aat.activities.AboutActivity
import ch.bailu.aat.activities.CockpitActivity
import ch.bailu.aat.activities.CockpitSplitActivity
import ch.bailu.aat.activities.CockpitTabletActivity
import ch.bailu.aat.activities.ExternalListActivity
import ch.bailu.aat.activities.MainActivity
import ch.bailu.aat.activities.MapActivity
import ch.bailu.aat.activities.NominatimActivity
import ch.bailu.aat.activities.OverlayListActivity
import ch.bailu.aat.activities.PreferencesActivity
import ch.bailu.aat.activities.TrackListActivity
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.ui.AppLayout
import org.mapsforge.core.model.BoundingBox

class ActivitySwitcher(private val callingActivity: Activity) {
    class Entry(
        val activityLabel: String,
        val activitySubLabel: String,
        val activityClass: Class<*>
    ) {
        constructor(activityLabel: String, activityClass: Class<*>) : this(activityLabel, "", activityClass)

        fun start(context: Context) {
            start(context, activityClass)
        }
    }

    init {
        init(callingActivity)
    }

    fun size(): Int {
        return entries.size
    }

    operator fun get(i: Int): Entry {
        return entries[i]
    }

    fun cycle() {
        if (DISABLE_ACTIVITY_CYCLING) return
        for (i in entries.indices) {
            if (entries[i].activityClass == callingActivity.javaClass) {
                var x = i + 1
                if (x > cyclable - 1) x = 0
                entries[x].start(callingActivity)
                callingActivity.finish()
                break
            }
        }
    }

    companion object {
        /**
         * Disable activity cycling because Android leaks memory
         * when switching activities
         */
        const val DISABLE_ACTIVITY_CYCLING = true

        private const val PHONE_CYCLABLE = 3
        private const val TABLET_CYCLABLE = 2

        private val entries: ArrayList<Entry> = ArrayList()
        private var cyclable = 0

        private fun getAbout(c: Context): Entry {
            return Entry(c.getString(R.string.intro_documentation), AboutActivity::class.java)
        }

        @JvmStatic
        operator fun get(obj: Any): Entry? {
            for (e in entries) {
                if (e.activityClass == obj.javaClass) return e
            }

            return null
        }

        @JvmStatic
        val defaultCockpit: Class<*>
            get() = if (entries.size > 0) entries[0].activityClass else MainActivity::class.java

        private fun init(context: Context) {
            if (entries.isEmpty()) {
                if (AppLayout.isTablet(context)) {
                    initTablet(context)
                } else {
                    initPhone(context)
                }
                initBoth(context)
            }
        }

        private fun initTablet(c: Context) {
            cyclable = TABLET_CYCLABLE
            entries.add(
                Entry(
                    c.getString(R.string.intro_cockpit_a),
                    CockpitTabletActivity::class.java
                )
            )
        }

        private fun initPhone(c: Context) {
            cyclable = PHONE_CYCLABLE
            entries.add(
                Entry(
                    c.getString(R.string.intro_cockpit_a),
                    c.getString(R.string.tt_cockpit_a),
                    CockpitActivity::class.java
                )
            )
            entries.add(
                Entry(
                    c.getString(R.string.intro_cockpit_b),
                    c.getString(R.string.tt_cockpit_b),
                    CockpitSplitActivity::class.java
                )
            )
        }

        private fun initBoth(c: Context) {
            entries.add(Entry(c.getString(R.string.intro_map), MapActivity::class.java))
            entries.add(Entry(c.getString(R.string.intro_list), TrackListActivity::class.java))
            entries.add(
                Entry(
                    c.getString(R.string.intro_overlay_list),
                    OverlayListActivity::class.java
                )
            )
            entries.add(
                Entry(
                    c.getString(R.string.intro_external_list),
                    ExternalListActivity::class.java
                )
            )
            entries.add(
                Entry(
                    c.getString(R.string.intro_nominatim),
                    c.getString(R.string.tt_info_nominatim),
                    NominatimActivity::class.java
                )
            )
            entries.add(
                Entry(
                    c.getString(R.string.intro_settings),
                    PreferencesActivity::class.java
                )
            )
            entries.add(getAbout(c))
        }

        @JvmStatic
        fun start(context: Context, activityClass: Class<*>) {
            val intent = Intent()
            AppIntent.setFile(intent, activityClass.simpleName)
            start(context, activityClass, intent)
        }

        @JvmStatic
        fun start(context: Context, activityClass: Class<*>, intent: Intent) {
            intent.setClass(context, activityClass)
            intent.action = activityClass.name
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }

        @JvmStatic
        fun start(context: Context, a: Class<*>, box: BoundingBox) {
            val intent = Intent()
            AppIntent.setBoundingBox(intent, box)
            start(context, a, intent)
        }
    }
}
