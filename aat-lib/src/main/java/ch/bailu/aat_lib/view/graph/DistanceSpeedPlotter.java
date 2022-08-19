package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.app.AppColor;
import ch.bailu.aat_lib.description.AverageSpeedDescription;
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP;
import ch.bailu.aat_lib.gpx.GpxDistanceWindow;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.lib.color.ColorInterface;

public class DistanceSpeedPlotter extends Plotter{

    private final StorageInterface storage;
    private final SolidUnit sunit;

    public DistanceSpeedPlotter(SolidUnit sunit) {
        this.storage = sunit.getStorage();
        this.sunit = sunit;
    }


    @Override
    public void plot(GraphCanvas canvas, PlotterConfig config) {
        GraphPlotter[] plotter = initPlotter(canvas, config.getWidth(), config.getHeight(), config.getList());
        GpxDistanceWindow window = new GpxDistanceWindow(config.getList());

        config.getLabels().setText(AppColor.HL_ORANGE, window.getLimitAsString(storage));

        new DistanceSpeedPainter(plotter, getAutoPause(), getMinDistance(config.getList(), config.getWidth()), window)
                .walkTrack(config.getList());

        plotter[0].drawXScale(5, sunit.getDistanceFactor(), config.isXLabelVisible());
        plotter[0].drawYScale(5, sunit.getSpeedFactor(), false);
    }

    @Override
    public void initLabels(LabelInterface labels) {
        labels.setText(ColorInterface.WHITE, Res.str().speed(), sunit.getSpeedUnit());
        labels.setText(AppColor.HL_BLUE, new AverageSpeedDescriptionAP(sunit.getStorage()).getLabel());
        labels.setText(AppColor.HL_GREEN, new AverageSpeedDescription(sunit.getStorage()).getLabel());
    }

    @Override
    public void setLimit(int firstPoint, int lastPoint) {

    }


    private float getMinDistance(GpxList list, int width) {
        float distForOnePixel = list.getDelta().getDistance() / Math.max(width, 1);
        return distForOnePixel * Config.SAMPLE_WIDTH_PIXEL;
    }


    private AutoPause getAutoPause() {
        int preset = new SolidPreset(storage).getIndex();
        final SolidAutopause spause = new SolidPostprocessedAutopause(storage, preset);
        return new AutoPause.Time(
                spause.getTriggerSpeed(),
                spause.getTriggerLevelMillis());
    }

    private GraphPlotter[] initPlotter(GraphCanvas canvas, int widht, int height, GpxList list) {
        int km_factor = (int) (list.getDelta().getDistance()/1000) + 1;
        int xscale = km_factor * 1000;

        float maxSpeed = list.getDelta().getAttributes().getAsFloat((MaxSpeed.INDEX_MAX_SPEED));
        return  initPlotter(canvas, xscale, widht, height, maxSpeed);
    }


    private GraphPlotter[] initPlotter(GraphCanvas canvas, int xscale, int width, int height, float maxSpeed) {
        GraphPlotter[] result = new GraphPlotter[3];
        for (int i=0; i<result.length; i++) {
            result[i] = new GraphPlotter(canvas, width, height, xscale);
            result[i].inlcudeInYScale(0f);
            result[i].inlcudeInYScale(maxSpeed);
        }
        return result;
    }
}
