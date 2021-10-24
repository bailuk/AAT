package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.app.window.MainWindow
import ch.bailu.aat_gtk.dispatcher.GtkBroadcaster
import ch.bailu.aat_gtk.logger.SL4JLogger
import ch.bailu.aat_gtk.service.GtkServices
import ch.bailu.aat_gtk.solid.GtkStorage
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.logger.BroadcastLogger
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    GTK.init()

    val app = Application(Str(GtkAppConfig.applicationId), ApplicationFlags.FLAGS_NONE)

    app.onActivate {
        App.attach(MainWindow(app, App.services, App.storage, App.broadcaster))
    }
    app.run(args.size, Strs(args))
}


object App {
    val storage = GtkStorage()
    val broadcaster = GtkBroadcaster()
    val services: ServicesInterface

    private val attachables = ArrayList<Attachable>()

    init {
        AppLog.set(SL4JLogger())
        AppGraphicFactory.set(GtkGraphicFactory.INSTANCE)
        AppConfig.setInstance(GtkAppConfig)

        AppLog.setError(
            BroadcastLogger(
                broadcaster,
                AppBroadcaster.LOG_ERROR,
                SL4JLogger()
            )
        )
        AppLog.setInfo(
            BroadcastLogger(
                broadcaster,
                AppBroadcaster.LOG_INFO,
                SL4JLogger()
            )
        )
        services = GtkServices(storage, broadcaster)
        services.trackerService.onStartStop()
    }


    fun exit(exitCode: Int) {
        services.trackerService.onStartStop()

        attachables.forEach { it.onDetached() }
        GtkStorage.save()
        exitProcess(exitCode)
    }

    fun attach(attachable: Attachable) {
        attachables.add(attachable)
    }
}