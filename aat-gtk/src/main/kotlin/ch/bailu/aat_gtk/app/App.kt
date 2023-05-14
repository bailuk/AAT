package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.lib.RuntimeInfo
import ch.bailu.aat_gtk.preferences.PreferenceLoadDefaults
import ch.bailu.aat_gtk.solid.GtkStorage
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.OverlaysSource
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.dispatcher.TrackerTimerSource
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.logger.BroadcastLoggerFactory
import ch.bailu.aat_lib.logger.PrintLnLoggerFactory
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import kotlin.system.exitProcess

fun main() {
    adwMain()
}

object App {
    val dispatcher = Dispatcher()

    fun setup() {
        AppConfig.setInstance(GtkAppConfig)
        RuntimeInfo.startLogging()
        AppLog.set(BroadcastLoggerFactory(GtkAppContext.broadcaster, PrintLnLoggerFactory()))
        AppGraphicFactory.set(GtkGraphicFactory.INSTANCE)
        PreferenceLoadDefaults(GtkAppContext)
    }

    fun setupDispatcher() {
        dispatcher.addSource(TrackerTimerSource(GtkAppContext.services, GtkTimer()))
        dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(
            OverlaysSource(
                GtkAppContext
            )
        )
    }

    fun exit(exitCode: Int) {
        dispatcher.onPause()
        GtkAppContext.services.trackerService.onStartStop()

        GtkStorage.save()
        exitProcess(exitCode)
    }
}
