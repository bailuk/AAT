package ch.bailu.aat.services

import android.app.Notification
import android.content.Context
import ch.bailu.aat_lib.service.ServicesInterface

interface ServiceContext : ServicesInterface {
    fun startForeground(id: Int, notification: Notification)
    fun stopForeground(b: Boolean)
    fun appendStatusText(builder: StringBuilder)

    fun getContext(): Context
}
