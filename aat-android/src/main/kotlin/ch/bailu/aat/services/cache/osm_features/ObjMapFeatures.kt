package ch.bailu.aat.services.cache.osm_features

import ch.bailu.aat.preferences.map.SolidOsmFeaturesList.Companion.getKeyList
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.lib.filter_list.AbsFilterList
import ch.bailu.aat_lib.lib.filter_list.AbsListItem
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.OnObject

class ObjMapFeatures(id: String) : Obj(id) {
    private var isLoaded = false
    private var size: Long = 0

    private inner class List {
        private val list = ArrayList<MapFeaturesListItem>(50)

        @Synchronized
        fun sync(f: AbsFilterList<AbsListItem>) {
            for (i in f.sizeAll() until list.size) {
                f.add(list[i])
            }
        }

        @Synchronized
        fun add(d: MapFeaturesListItem) {
            list.add(d)
        }
    }

    private val list: List = List()

    override fun onInsert(sc: AppContext) {
        super.onInsert(sc)
        sc.services.getBackgroundService().process(ListLoader(getID()))
    }

    override fun onDownloaded(id: String, url: String, sc: AppContext) {}
    override fun onChanged(id: String, sc: AppContext) {}
    override fun getSize(): Long {
        return size
    }

    fun syncList(filterList: AbsFilterList<AbsListItem>) {
        list.sync(filterList)
    }

    override fun isReadyAndLoaded(): Boolean {
        return isLoaded
    }

    class ListLoader(private val ID: String) : BackgroundTask() {
        private var doBroadcast = 0

        override fun bgOnProcess(appContext: AppContext): Long {
            var size = 0L

            object : OnObject(appContext, ID, ObjMapFeatures::class.java) {
                override fun run(handle: Obj) {
                    if (handle is ObjMapFeatures) {
                        parseMapFeatures(appContext, handle)
                        handle.isLoaded = true
                        appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, ID)
                        size = handle.size
                    }
                }
            }
            return size
        }

        private fun parseMapFeatures(appContext: AppContext, handle: ObjMapFeatures) {
            val keyList = getKeyList(ID)

            MapFeaturesParser(appContext.assets, { file ->
                    keyList.isEmpty || keyList.hasKey(file.lowercase())
                },
                { parser ->
                    val d = MapFeaturesListItem(parser)
                    handle.size += d.length() * 2L
                    handle.list.add(d)
                    doBroadcast++
                    if (doBroadcast > 10) {
                        doBroadcast = 0
                        appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, ID)
                }
            })
        }
    }

    class Factory(val ID: String) : Obj.Factory() {
        override fun factory(id: String, cs: AppContext): Obj {
            return ObjMapFeatures(ID)
        }
    }

    companion object {
        val ID_FULL: String = ObjMapFeatures::class.java.simpleName
        val ID_SMALL = ObjMapFeatures::class.java.simpleName + "/s"
    }
}
