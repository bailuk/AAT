package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.elevation.tile.MultiCell

class ObjHillShadeColorTable : Obj(ID) {
    private var table: HillShadeColorTable? = null

    override fun onInsert(appContext: AppContext) {
        appContext.services.getBackgroundService().process(TableInitializer())
    }

    override fun onDownloaded(id: String, url: String, appContext: AppContext) {}

    override fun onChanged(id: String, appContext: AppContext) {}

    override fun isReadyAndLoaded(): Boolean {
        return table is HillShadeColorTable
    }

    override fun getSize(): Long {
        return HillShadeColorTable.TABLE_SIZE.toLong()
    }

    fun getColor(multiCell: MultiCell): Int {
        val table = table
        return if (table is HillShadeColorTable) {
            table.getColor(multiCell)
        } else {
            0
        }
    }

    private inner class TableInitializer : BackgroundTask() {
        override fun bgOnProcess(appContext: AppContext): Long {
            table = HillShadeColorTable()
            appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, ID)
            return TABLE_SIZE.toLong() * 4
        }
    }

    companion object {
        @JvmField
        val ID: String = ObjHillShadeColorTable::class.java.simpleName

        private const val TABLE_DIM = 500
        private const val TABLE_SIZE = TABLE_DIM * TABLE_DIM


        @JvmField
        val FACTORY: Factory = object : Factory() {
            override fun factory(id: String, appContext: AppContext): Obj {
                return ObjHillShadeColorTable()
            }
        }
    }
}
