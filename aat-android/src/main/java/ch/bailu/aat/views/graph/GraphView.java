package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.view.graph.LabelInterface;
import ch.bailu.aat_lib.view.graph.Plotter;
import ch.bailu.aat_lib.view.graph.PlotterConfig;

public class GraphView extends ViewGroup implements OnContentUpdatedInterface, PlotterConfig {

    private final AndroidAppDensity density;
    private final UiTheme theme;

    private GpxList gpxCache = GpxList.NULL_TRACK;
    private int nodeIndex = -1;

    private final LabelOverlay ylabel, xlabel;

    private final Plotter plotter;

    public GraphView(Context context, Plotter plotter, UiTheme theme) {
        super(context);

        this.theme = theme;
        this.density = new AndroidAppDensity(getContext());
        this.plotter = plotter;

        setWillNotDraw(false);

        SolidUnit sunit = new SolidUnit(new Storage(context));

        xlabel = new LabelOverlay(context, Gravity.START | Gravity.BOTTOM);
        ylabel = new LabelOverlay(context, Gravity.END | Gravity.TOP);

        xlabel.setGravity(Gravity.BOTTOM);
        xlabel.setText(Color.WHITE, R.string.distance, sunit.getDistanceUnit());

        addView(xlabel);
        addView(ylabel);

        setBackgroundColor(theme.getGraphBackgroundColor());

        plotter.initLabels(ylabel);
    }



    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        onContentUpdated(iid, info, -1);
    }

    public void onContentUpdated(int iid, GpxInformation info, int i) {
        gpxCache = info.getGpxList();
        nodeIndex = i;

        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getWidth() > 0 && getHeight() > 0) {
            plotter.plot(new AndroidCanvas(canvas, density, theme), this);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        xlabel.layout(0,0, r-l, b-t);
        ylabel.layout(0,0, r-l, b-t);
    }

    public GraphView hideXLabel() {
        xlabel.setVisibility(GONE);
        return this;
    }

    @Override
    public GpxList getList() {
        return gpxCache;
    }


    @Override
    public int getIndex() {
        return nodeIndex;
    }

    public boolean isXLabelVisible() {
        return xlabel.getVisibility() == VISIBLE;
    }

    @Override
    public LabelInterface getLabels() {
        return ylabel;
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height = MeasureSpec.getSize(hSpec);

        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);


        xlabel.measure(wSpec, hSpec);

        ylabel.setTextSizeFromHeight(height);
        ylabel.measure(wSpec, hSpec);
        setMeasuredDimension(width, height);
    }

    public void showLabel(boolean b) {
        if (b) {
            xlabel.setVisibility(VISIBLE);
            ylabel.setVisibility(VISIBLE);
        } else {
            xlabel.setVisibility(GONE);
            ylabel.setVisibility(GONE);
        }
    }

    public void setVisibility(GpxInformation info) {
        if (info.isLoaded() && info.getType() == GpxType.ROUTE || info.getType() == GpxType.TRACK) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }


    public GraphView connect(DispatcherInterface di, int...iid) {
        di.addTarget(this, iid);
        return this;
    }

    public void setLimit(int firstPoint, int lastPoint) {
        plotter.setLimit(firstPoint, lastPoint);
    }
}
