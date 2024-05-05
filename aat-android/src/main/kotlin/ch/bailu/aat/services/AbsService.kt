package ch.bailu.aat.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.text.format.DateFormat
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat_lib.logger.AppLog
import java.util.Date

abstract class AbsService : Service() {
    private var creations = 0
    private var startTime: Long = 0
    private var up = false
    private var lock = 0

    @Synchronized
    fun lock(): Boolean {
        if (up) {
            lock++
            try {
                startService(Intent(this, OneService::class.java))
            } catch (e: IllegalStateException) {
                AppLog.w(this, e)
            }
            lazyOff.cancel()
        }
        return up
    }

    @Synchronized
    fun free() {
        if (up) {
            lock--
            if (lock == 0) {
                lazyOff.kick((15 * 1000).toLong()) { this.stopService() }
            } else if (lock < 0) {
                AppLog.w(this, "lock < 0 !!!")
            }
        }
    }

    private val locks: MutableSet<String> = HashSet()
    private val lazyOff = AndroidTimer()

    @Synchronized
    private fun stopService() {
        if (lock == 0) {
            lazyOff.cancel()
            stopSelf()
        } else if (lock < 0) {
            AppLog.w(this, "lock < 0 !!!")
        }
    }

    @Synchronized
    fun lock(resource: String) {
        if (locks.add(resource)) {
            lock()
        }
    }

    @Synchronized
    fun free(resource: String) {
        if (locks.remove(resource)) free()
    }

    inner class CommonBinder : Binder() {
        val service: AbsService
            get() = this@AbsService
    }

    @Synchronized
    override fun onCreate() {
        super.onCreate()
        up = true
        creations++
        startTime = System.currentTimeMillis()
    }

    protected fun onDestroyCalled() {
        up = false
    }

    @Synchronized
    override fun onDestroy() {
        creations--
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return CommonBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        return false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    open fun appendStatusText(builder: StringBuilder) {
        builder.append("<h1>")
        builder.append(javaClass.simpleName)
        builder.append("</h1>")
        builder.append("<p>Start time: ")
        builder.append(formatDate(startTime))
        builder.append(" - ")
        builder.append(formatTime(startTime))
        builder.append("<br>Created services: ")
        builder.append(creations)
        builder.append("</p>")
    }

    private fun formatDate(time: Long): String {
        val date = Date(time)
        val dateFormat = DateFormat.getDateFormat(applicationContext)
        return dateFormat.format(date)
    }

    private fun formatTime(time: Long): String {
        val date = Date(time)
        val dateFormat = DateFormat.getTimeFormat(applicationContext)
        return dateFormat.format(date)
    }
}
