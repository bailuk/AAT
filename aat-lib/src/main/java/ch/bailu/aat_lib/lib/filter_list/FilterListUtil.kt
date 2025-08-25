package ch.bailu.aat_lib.lib.filter_list

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.search.poi.PoiListItem
import ch.bailu.foc.Foc
import org.mapsforge.poi.storage.PoiCategoryManager
import org.mapsforge.poi.storage.PoiPersistenceManager
import org.mapsforge.poi.storage.UnknownPoiCategoryException
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object FilterListUtil {
    private const val LINE_LIMIT = 500
    private const val LINE_EST = 50

    @Throws(IOException::class)
    fun writeSelected(filterList: FilterList, file: Foc) {
        OutputStreamWriter(BufferedOutputStream(file.openW())).use { out ->
            for (e in 0 until filterList.sizeVisible()) {
                if (filterList.getFromVisible(e).isSelected()) {
                    val keys = filterList.getFromVisible(e).getKeys()

                    for (k in 0 until keys.size() - 1) {
                        out.write(keys.getKey(k))
                        out.write(' '.code)
                    }
                    out.write('\n'.code)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun readSelected(filterList: FilterList, file: Foc) {
        InputStreamReader(BufferedInputStream(file.openR())).use { `in` ->
            val builder = StringBuilder(LINE_EST)
            var c = 0
            while (c > -1) {
                c = `in`.read()

                if (c < 0 || c == '\n'.code) {
                    val keys = KeyList(builder.toString())

                    if (keys.size() > 0) {
                        select(filterList, keys)
                    }
                    builder.setLength(0)
                } else if (builder.length < LINE_LIMIT) {
                    builder.append(c.toChar())
                }
            }
        }
    }

    fun readList(filterList: FilterList, appContext: AppContext, db: String, selected: Foc) {
        var persistenceManager: PoiPersistenceManager? = null

        try {
            filterList.clear()
            if (db.trim().isNotEmpty()) {
                persistenceManager = appContext.getPoiPersistenceManager(db)
                val categoryManager = persistenceManager.categoryManager
                readList(filterList, categoryManager)
                readSelected(filterList, selected)
            }
        } catch (e: Exception) {
            AppLog.e(selected, "Load " + db + ": " + e.message)
        } finally {
            persistenceManager?.close()
        }
    }

    @Throws(UnknownPoiCategoryException::class)
    private fun readList(filterList: FilterList, categoryManager: PoiCategoryManager) {
        val root = categoryManager.rootCategory

        for (summary in root.children) {
            val summaryEntry = PoiListItem(summary)
            filterList.add(summaryEntry)

            for (category in summary.children) {
                filterList.add(PoiListItem(category, summaryEntry))
            }
        }
    }

    private fun select(filterList: FilterList, keys: KeyList) {
        for (e in 0 until filterList.sizeAll()) {
            if (filterList.getFromAll(e).getKeys().fits(keys)) {
                filterList.getFromAll(e).setSelected(true)
                break
            }
        }
    }
}
