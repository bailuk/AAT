package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.logger.SL4JLogger
import ch.bailu.aat_gtk.solid.GtkStorage
import ch.bailu.aat_gtk.ui.window.MainWindow
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.logger.BroadcastLogger
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ActionMap
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.type.Str
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import kotlin.system.exitProcess

fun main() {
    GTK.init()
    App.run()
}


object App {
    private val attachables = ArrayList<Attachable>()

    init {
        AppLog.set(SL4JLogger())
        AppGraphicFactory.set(GtkGraphicFactory.INSTANCE)
        AppConfig.setInstance(GtkAppConfig)

        AppLog.setError(
            BroadcastLogger(
                GtkAppContext.broadcaster,
                AppBroadcaster.LOG_ERROR,
                SL4JLogger()
            )
        )
        AppLog.setInfo(
            BroadcastLogger(
                GtkAppContext.broadcaster,
                AppBroadcaster.LOG_INFO,
                SL4JLogger()
            )
        )
    }

    fun run() {
        val app = Application(Str(GtkAppConfig.applicationId), ApplicationFlags.FLAGS_NONE)

        app.onActivate {
            try {
                val window = MainWindow(ActionMap(app.cast()), ApplicationWindow(app), GtkAppContext.services, GtkAppContext.broadcaster)
                attach(window)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        app.run(0, null)
    }

    fun exit(exitCode: Int) {
        GtkAppContext.services.trackerService.onStartStop()

        attachables.forEach { it.onDetached() }
        GtkStorage.save()
        exitProcess(exitCode)
    }

    fun attach(attachable: Attachable) {
        attachables.add(attachable)
    }
}
