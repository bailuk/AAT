package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.GResource
import ch.bailu.aat_gtk.lib.RuntimeInfo
import ch.bailu.aat_gtk.preferences.PreferenceLoadDefaults
import ch.bailu.aat_gtk.solid.GtkStorage
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.view.toplevel.MainWindow
import ch.bailu.aat_lib.Configuration
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.TrackerTimerSource
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.logger.BroadcastLoggerFactory
import ch.bailu.aat_lib.logger.PrintLnLoggerFactory
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.type.Strs
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import kotlin.system.exitProcess

fun main() {
    setup()

    val app = Application(GtkAppConfig.appId, ApplicationFlags.FLAGS_NONE)

    app.onActivate {
        val dispatcher = Dispatcher()
        MainWindow(app, GtkAppContext, dispatcher).window.show()
        setupDispatcher(dispatcher)
    }

    app.run(1, Strs(arrayOf(Configuration.appName)))
}

private fun setup() {
    GResource.loadAndRegister(Strings.appGResource)

    AppConfig.setInstance(GtkAppConfig)
    RuntimeInfo.startLogging()
    AppLog.set(BroadcastLoggerFactory(GtkAppContext.broadcaster, PrintLnLoggerFactory()))
    AppGraphicFactory.set(GtkGraphicFactory.INSTANCE)
    PreferenceLoadDefaults(GtkAppContext)
}

private fun setupDispatcher(dispatcher: Dispatcher) {
    dispatcher.addSource(TrackerTimerSource(GtkAppContext.services, GtkTimer()))
    dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
    dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster))
    dispatcher.onResumeWithService()
}


fun exit(dispatcher: Dispatcher, exitCode: Int) {
    dispatcher.onPauseWithService()
    dispatcher.onDestroy()

    GtkAppContext.services.getTrackerService().onStartStop()

    GtkStorage.save()
    exitProcess(exitCode)
}
