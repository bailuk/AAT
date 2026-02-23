package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP
import ch.bailu.aat_lib.gpx.GpxDistanceWindow
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed
import ch.bailu.aat_lib.lib.color.ColorInterface
import ch.bailu.aat_lib.preferences.SolidAutopause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.resources.Res.str
import kotlin.math.max

class DistanceSpeedPlotter(private val sunit: SolidUnit) : Plotter() {
    private val storage: StorageInterface = sunit.getStorage()


    override fun plot(canvas: GraphCanvas, config: PlotterConfig) {
        val plotter = initPlotter(canvas, config.getWidth(), config.getHeight(), config.getList())
        val window = GpxDistanceWindow(config.getList())

        config.getLabels().setText(AppColor.HL_ORANGE, window.getLimitAsString(storage))

        DistanceSpeedPainter(plotter, this.autoPause, getMinDistance(config.getList(), config.getWidth()), window)
            .walkTrack(config.getList())

        plotter[0].drawXScale(5, sunit.distanceFactor, config.isXLabelVisible())
        plotter[0].drawYScale(5, sunit.speedFactor, false)
    }

    override fun initLabels(labels: LabelInterface) {
        labels.setText(ColorInterface.WHITE, str().speed(), sunit.speedUnit)
        labels.setText(AppColor.HL_BLUE, AverageSpeedDescriptionAP(sunit.getStorage()).getLabel())
        labels.setText(AppColor.HL_GREEN, AverageSpeedDescription(sunit.getStorage()).getLabel())
    }

    override fun setLimit(firstPoint: Int, lastPoint: Int) {}

    private fun getMinDistance(list: GpxList, width: Int): Float {
        val distForOnePixel = list.getDelta().getDistance() / max(width, 1)
        return distForOnePixel * Config.SAMPLE_WIDTH_PIXEL
    }

    private val autoPause: AutoPause
        get() {
            val preset = SolidPreset(storage).index
            val spause: SolidAutopause = SolidPostprocessedAutopause(storage, preset)
            return AutoPause.Time(
                spause.triggerSpeed,
                spause.triggerLevelMillis
            )
        }

    private fun initPlotter(
        canvas: GraphCanvas,
        width: Int,
        height: Int,
        list: GpxList
    ): List<GraphPlotter> {
        val kmFactor = (list.getDelta().getDistance() / 1000).toInt() + 1
        val xscale = kmFactor * 1000
        val maxSpeed = list.getDelta().getAttributes().getAsFloat((MaxSpeed.INDEX_MAX_SPEED))
        return initPlotter(canvas, xscale, width, height, maxSpeed)
    }
    private fun initPlotter(
        canvas: GraphCanvas,
        xscale: Int,
        width: Int,
        height: Int,
        maxSpeed: Float
    ): List<GraphPlotter> {
        val result = ArrayList<GraphPlotter>(PLOT_COUNT)
        repeat(PLOT_COUNT) {
            val plotter = GraphPlotter(canvas, width, height, xscale.toFloat())
            plotter.includeInYScale(0f)
            plotter.includeInYScale(maxSpeed)
            result.add(plotter)
        }
        return result
    }

    companion object {
        const val PLOT_COUNT = 3
    }
}
