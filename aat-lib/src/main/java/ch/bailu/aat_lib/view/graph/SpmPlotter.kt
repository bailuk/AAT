package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.description.CadenceDescription
import ch.bailu.aat_lib.description.HeartRateDescription
import ch.bailu.aat_lib.description.StepRateDescription
import ch.bailu.aat_lib.gpx.attributes.SampleRate
import ch.bailu.aat_lib.preferences.general.SolidUnit
import kotlin.math.max

class SpmPlotter(private val sunit: SolidUnit) : Plotter() {
    override fun plot(canvas: GraphCanvas, config: PlotterConfig) {
        var max = 0
        val min = 25
        val kmFactor = (config.getList().getDelta().getDistance() / 1000).toInt() + 1

        for (e in ENTRIES) {
            e.setPlotter(kmFactor, canvas, config.getWidth(), config.getHeight())
            max = max(max, e.getMax(config.getList()))
        }

        if (max > 0) {
            for (e in ENTRIES) {
                e.getPlotter()?.includeInYScale(min.toFloat())
                e.getPlotter()?.includeInYScale(max.toFloat())
                e.getPlotter()?.roundYScale(25)
            }

            SpmGraphPainter(
                ENTRIES,
                config.getList().getDelta().getDistance().toInt() / max(config.getWidth(), 1)
            ).walkTrack(config.getList())

            ENTRIES[0].getPlotter()?.drawYScale(5, 1f, false)
        }

        ENTRIES[0].getPlotter()?.drawXScale(5, sunit.distanceFactor, config.isXLabelVisible())
    }

    override fun initLabels(labels: LabelInterface) {
        for (e in ENTRIES) e.setLabelText(labels)
    }

    override fun setLimit(firstPoint: Int, lastPoint: Int) {}

    companion object {
        val ENTRIES: Array<SpmEntry> = createEntries()
        private fun createEntries(): Array<SpmEntry> {
            return arrayOf(
                SpmEntry(
                    AppColor.HL_BLUE,
                    HeartRateDescription(),
                    SampleRate.HeartRate.INDEX_MAX_HR,
                    *SampleRate.HeartRate.GPX_KEYS
                ),
                SpmEntry(
                    AppColor.HL_GREEN,
                    CadenceDescription(),
                    SampleRate.Cadence.INDEX_MAX_CADENCE,
                    *SampleRate.Cadence.GPX_KEYS
                ),
                SpmEntry(
                    AppColor.HL_ORANGE,
                    StepRateDescription(),
                    SampleRate.StepsRate.INDEX_MAX_SPM,
                    *SampleRate.StepsRate.GPX_KEYS
                )
            )
        }
    }
}
