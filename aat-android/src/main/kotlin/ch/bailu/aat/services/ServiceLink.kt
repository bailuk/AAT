package ch.bailu.aat.services

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import ch.bailu.aat.services.AbsService.CommonBinder
import ch.bailu.aat_lib.service.tileremover.TileRemoverService
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface
import ch.bailu.aat_lib.service.elevation.ElevationServiceInterface
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.render.RenderServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface
import java.io.Closeable


abstract class ServiceLink(private val context: Context) : ServiceContext, ServiceConnection,
    Closeable {
    class ServiceNotUpError(service: Class<*>) :
        RuntimeException("Service '" + service.simpleName + "' is not running.*")

    private var lock = 0
    private var service: ServiceContext? = null
    private var bound = false



    override fun getContext(): Context {
        return context
    }

    fun up() {
        bindService()
    }

    override fun onServiceConnected(className: ComponentName, binder: IBinder) {
        grabService(binder)
        onServiceUp()
    }

    override fun onServiceDisconnected(name: ComponentName) {
        service = null
    }

    fun down() {
        releaseService()
        unbindService()
    }

    private fun grabService(binder: IBinder) {
        if (binder is CommonBinder) {

            val service = binder.service
            if (service is ServiceContext) {
                this.service = service
                service.lock(ServiceLink::class.java.simpleName)
            }
        }
    }

    @Synchronized
    private fun releaseService() {
        try {
            while (service != null) {
                if (lock == 0) {
                    service?.free(ServiceLink::class.java.simpleName)
                    service = null
                } else {
                    (this as Object).wait()
                }
            }
        } catch (e: Exception) {
            AppLog.e(context, e)
        }
    }

    private fun bindService() {
        if (!bound) {
            bound = true
            try {
                context.bindService(
                    Intent(
                        context,
                        OneService::class.java
                    ), this, Context.BIND_AUTO_CREATE
                )
            } catch (e: Exception) {
                AppLog.e(context, e)
            }
        }
    }

    private fun unbindService() {
        if (bound) {
            bound = false
            try {
                context.unbindService(this)
            } catch (e: Exception) {
                AppLog.e(context, e)
            }
        }
    }

    private val isUp: Boolean
        get() = service != null

    abstract fun onServiceUp()
    private fun getService(): ServiceContext? {
        return if (isUp) service else throw ServiceNotUpError(OneService::class.java)
    }

    @Synchronized
    override fun lock(): Boolean {
        if (isUp && getService()!!.lock()) {
            lock++
            return true
        }
        return false
    }

    @Synchronized
    override fun free() {
        if (isUp) {
            getService()?.free()
            lock--
            if (lock == 0) (this as Object).notifyAll()
        }
    }

    @Synchronized
    override fun lock(resource: String) {
        if (isUp) getService()?.lock(resource)
    }

    @Synchronized
    override fun free(resource: String) {
        if (isUp) getService()?.free(resource)
    }

    override fun close() {
        down()
    }

    override fun getTrackerService(): TrackerServiceInterface {
        return getService()!!.getTrackerService()
    }

    override fun getLocationService(): LocationServiceInterface {
        return getService()!!.getLocationService()
    }

    override fun getBackgroundService(): BackgroundServiceInterface {
        return getService()!!.getBackgroundService()
    }

    override fun getCacheService(): CacheServiceInterface {
        return getService()!!.getCacheService()
    }

    override fun getElevationService(): ElevationServiceInterface {
        return getService()!!.getElevationService()
    }

    override fun getIconMapService(): IconMapServiceInterface {
        return getService()!!.getIconMapService()
    }

    override fun getDirectoryService(): DirectoryServiceInterface {
        return getService()!!.getDirectoryService()
    }

    override fun getRenderService(): RenderServiceInterface {
        return getService()!!.getRenderService()
    }


    override fun getTileRemoverService(): TileRemoverService {
        return getService()!!.getTileRemoverService()
    }

    override fun getSensorService(): SensorServiceInterface {
        return getService()!!.getSensorService()
    }

    override fun startForeground(id: Int, notification: Notification) {
        getService()!!.startForeground(id, notification)
    }

    override fun stopForeground(b: Boolean) {
        getService()!!.stopForeground(b)
    }

    override fun appendStatusText(builder: StringBuilder) {
        getService()!!.appendStatusText(builder)
    }
}
