package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.service.elevation.tile.MultiCell
import ch.bailu.aat_lib.util.Limit
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HillShadeColorTable {
    private val table = Array(TABLE_DIM) { IntArray(TABLE_DIM) }

    init {
        for (x in table.indices) {
            for (y in table.indices) {
                table[x][y] = hillShadeColor(indexToDelta(x), indexToDelta(y))
            }
        }
    }

    fun getColor(multiCell: MultiCell): Int {
        val x = deltaToIndex(multiCell.deltaZX())
        val y = deltaToIndex(multiCell.deltaZY())
        return table[x][y]
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

    /**
     * Cairo expects pre-multiplied alpha. Else there will be pixel corruption when
     * saving images.
     */
    private fun preMultipliedAlpha(alpha: Int, color: Int): Int {
        return ((color * alpha).toDouble() / 255.0).toInt()
    }

    private fun hillShadeColor(dzx: Double, dzy: Double): Int {
        val alpha = hillShadeAlpha(dzx, dzy, ALPHA_LIMIT)
        val color = preMultipliedAlpha(alpha, COLOR)
        return ARGB(alpha, color, color, color).toInt()
    }

    private fun hillShadeAlpha(dzx: Double, dzy: Double, limit: Int): Int {
        val slope = slopeRad(dzx, dzy)
        val shade = (limit * ((ZENITH_COS * cos(slope)) +
                (ZENITH_SIN * sin(slope) * cos(AZIMUTH_RAD - aspectRad(dzx, dzy))))).toInt()

        return Limit.clamp(limit - shade, 0, MAX_DARKNESS)
    }

    companion object {
        private const val ALPHA_LIMIT = 175
        private const val DELTA_SCALE = 50.0
        private const val MAX_DARKNESS = 255
        private const val TABLE_DIM = 500
        private const val TABLE_HDIM = TABLE_DIM / 2
        const val TABLE_SIZE = TABLE_DIM * TABLE_DIM

        private const val COLOR = 50
        /**
         * Source:
         * http://edndoc.esri.com/arcobjects/9.2/net/shared/geoprocessing/spatial_analyst_tools/how_hillshade_works.htm
         */
        private const val ALTITUDE_DEG = 45.0

        private const val AZIMUTH_DEG = 315.0
        private const val AZIMUTH_MATH = 360f - AZIMUTH_DEG + 90f
        private const val ZENITH_DEG = (90.0 - ALTITUDE_DEG)
        private const val DOUBLE_PI = Math.PI * 2.0
        private const val HALF_PI = Math.PI * 0.5
        private const val ONEHALF_PI = Math.PI * 1.5

        private val AZIMUTH_RAD = Math.toRadians(AZIMUTH_MATH)
        private val ZENITH_RAD = Math.toRadians(ZENITH_DEG)
        private val ZENITH_COS = cos(ZENITH_RAD)
        private val ZENITH_SIN = sin(ZENITH_RAD)

        private fun indexToDelta(i: Int): Double {
            return (i - TABLE_HDIM).toDouble() / DELTA_SCALE
        }

        private fun deltaToIndex(delta: Int): Int {
            return Limit.clamp(delta, TABLE_HDIM * -1, TABLE_HDIM-1) + TABLE_HDIM
        }
    }
}
