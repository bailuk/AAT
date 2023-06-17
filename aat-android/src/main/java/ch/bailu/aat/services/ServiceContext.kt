package ch.bailu.aat.services

import android.app.Notification
import ch.bailu.aat.services.tileremover.TileRemoverService
import ch.bailu.aat.util.ContextWrapperInterface
import ch.bailu.aat_lib.service.ServicesInterface

interface ServiceContext : ContextWrapperInterface, ServicesInterface {
    override fun lock(resource: String)
    override fun free(resource: String)
    override fun lock(): Boolean
    override fun free()

    // TODO move to ServicesInterface
    fun getTileRemoverService(): TileRemoverService

    fun startForeground(id: Int, notification: Notification)
    fun stopForeground(b: Boolean)
    fun appendStatusText(builder: StringBuilder)
}
