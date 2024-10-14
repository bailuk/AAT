package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.elevation.tile.MultiCell
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class ObjHillShadeColorTable : Obj(ID) {
    private val table = Array(TABLE_DIM) { ByteArray(TABLE_DIM) }

    private var isInitialized = false

    override fun onInsert(appContext: AppContext) {
        appContext.services.getBackgroundService().process(TableInitializer())
    }

    override fun onDownloaded(id: String, url: String, appContext: AppContext) {}

    override fun onChanged(id: String, appContext: AppContext) {}

    override fun isReadyAndLoaded(): Boolean {
        return isInitialized
    }

    override fun getSize(): Long {
        return TABLE_SIZE.toLong()
    }


    fun getColor(multiCell: MultiCell): Int {
        val x = deltaToIndex(cutDelta(multiCell.delta_zx()))
        val y = deltaToIndex(cutDelta(multiCell.delta_zy()))
        val alpha = table[x][y].toInt() and 0xFF

        return (alpha shl 24) or GRAY
    }

    private inner class TableInitializer : BackgroundTask() {
        override fun bgOnProcess(appContext: AppContext): Long {
            for (x in 0 until TABLE_DIM) {
                for (y in 0 until TABLE_DIM) {
                    table[x][y] = hillShade(indexToDelta(x), indexToDelta(y))
                }
            }

            isInitialized = true
            appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, ID)

            return TABLE_SIZE.toLong()
        }

        private fun hillShade(dzx: Float, dzy: Float): Byte {
            val slope = slopeRad(dzx.toDouble(), dzy.toDouble())

            var shade = (255.0 * ((ZENITH_COS * cos(slope)) +
                    (ZENITH_SIN * sin(slope) * cos(
                        AZIMUTH_RAD - aspectRad(
                            dzx.toDouble(),
                            dzy.toDouble()
                        )
                    )))).toInt()


            shade = (255 - MAX_DARKNESS.coerceAtLeast(shade))
            return shade.toByte()
        }


        private fun slopeRad(dzx: Double, dzy: Double): Double {
            return atan(sqrt(dzx * dzx + dzy * dzy))
        }


        private fun aspectRad(dzx: Double, dzy: Double): Double {
            var ret = 0.0

            if (dzx != 0.0) {
                ret = atan2(dzy, -1.0 * dzx)

                if (ret < 0) {
                    ret += DOUBLE_PI
                }
            } else {
                if (dzy > 0) {
                    ret = HALF_PI
                } else if (dzy < 0) {
                    ret = ONEHALF_PI
                }
            }
            return ret
        }
    }


    companion object {
        @JvmField
        val ID: String = ObjHillShadeColorTable::class.java.simpleName

        private const val MAX_DARKNESS = 50
        private const val TABLE_DIM = 500
        private const val TABLE_HDIM = TABLE_DIM / 2
        private const val TABLE_SIZE = TABLE_DIM * TABLE_DIM

        private const val MIN_DELTA = -250
        private const val MAX_DELTA = 240

        private const val COLOR = 50
        private const val GRAY = (COLOR shl 16) or (COLOR shl 8) or COLOR

        /**
         * Source:
         * http://edndoc.esri.com/arcobjects/9.2/net/shared/geoprocessing/spatial_analyst_tools/how_hillshade_works.htm
         */
        private const val ALTITUDE_DEG = 45.0

        private const val AZIMUTH_DEG = 315.0
        private const val AZIMUTH_MATH = 360f - AZIMUTH_DEG + 90f
        private const val ZENITH_DEG = (90.0 - ALTITUDE_DEG)
        private const val DOUBLE_PI = Math.PI * 2.0
        private const val HALF_PI = Math.PI / 2.0
        private const val ONEHALF_PI = DOUBLE_PI - HALF_PI

        private val AZIMUTH_RAD = Math.toRadians(AZIMUTH_MATH)

        private val ZENITH_RAD = Math.toRadians(ZENITH_DEG)
        private val ZENITH_COS = cos(ZENITH_RAD)
        private val ZENITH_SIN = sin(ZENITH_RAD)



        private fun indexToDelta(i: Int): Float {
            return (i - TABLE_HDIM) / 100f
        }

        private fun deltaToIndex(d: Int): Int {
            return (d + TABLE_HDIM)
        }


        private fun cutDelta(delta: Int): Int {
            var result = delta
            result = max(MIN_DELTA.toDouble(), result.toDouble()).toInt()
            result = min(MAX_DELTA.toDouble(), result.toDouble()).toInt()
            return result
        }


        @JvmField
        val FACTORY: Factory = object : Factory() {
            override fun factory(id: String, appContext: AppContext): Obj {
                return ObjHillShadeColorTable()
            }
        }
    }
}
