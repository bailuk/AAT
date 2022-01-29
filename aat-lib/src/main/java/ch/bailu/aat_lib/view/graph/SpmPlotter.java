package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.app.AppColor;
import ch.bailu.aat_lib.description.CadenceDescription;
import ch.bailu.aat_lib.description.HeartRateDescription;
import ch.bailu.aat_lib.description.StepRateDescription;
import ch.bailu.aat_lib.gpx.attributes.SampleRate;
import ch.bailu.aat_lib.preferences.general.SolidUnit;

public class SpmPlotter extends Plotter {

    public final static SpmEntry[] ENTRIES = createEntries();
    private final SolidUnit sunit;

    public SpmPlotter(SolidUnit sunit) {
        this.sunit = sunit;
    }

    private static SpmEntry[] createEntries() {
        return new SpmEntry[]{
                new SpmEntry(AppColor.HL_BLUE,
                        new HeartRateDescription(),
                        SampleRate.HeartRate.INDEX_MAX_HR,
                        SampleRate.HeartRate.GPX_KEYS),

                new SpmEntry(AppColor.HL_GREEN,
                        new CadenceDescription(),
                        SampleRate.Cadence.INDEX_MAX_CADENCE,
                        SampleRate.Cadence.GPX_KEYS),


                new SpmEntry(AppColor.HL_ORANGE,
                        new StepRateDescription(),
                        SampleRate.StepsRate.INDEX_MAX_SPM,
                        SampleRate.StepsRate.GPX_KEYS)
        };
    }

    @Override
    public void plot(GraphCanvas canvas, PlotterConfig config) {
        int max = 0;
        int min = 25;
        int km_factor = (int) (config.getList().getDelta().getDistance() / 1000) + 1;

        for (SpmEntry e : ENTRIES) {
            e.setPlotter(km_factor, canvas, config.getWidth(), config.getHeight());
            max = Math.max(max, e.getMax(config.getList()));
        }

        if (max > 0) {
            for (SpmEntry e: ENTRIES) {
                e.getPlotter().inlcudeInYScale(min);
                e.getPlotter().inlcudeInYScale(max);
                e.getPlotter().roundYScale(25);
            }

            new SpmGraphPainter(
                    ENTRIES,
                    (int)config.getList().getDelta().getDistance() / Math.max(config.getWidth(),1)
            ).walkTrack(config.getList());

            ENTRIES[0].getPlotter().drawYScale(5, 1, false);
        }

        ENTRIES[0].getPlotter().drawXScale(5, sunit.getDistanceFactor(), config.isXLabelVisible());
    }

    @Override
    public void initLabels(LabelInterface labels) {
        for (SpmEntry e : SpmPlotter.ENTRIES)
            e.setLabelText(labels);
    }

    @Override
    public void setLimit(int firstPoint, int lastPoint) {

    }

}
