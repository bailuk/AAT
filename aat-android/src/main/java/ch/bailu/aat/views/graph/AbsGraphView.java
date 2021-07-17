package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.general.SolidUnit;

public abstract class AbsGraphView extends ViewGroup implements OnContentUpdatedInterface {

    public final static int SAMPLE_WIDTH_PIXEL=5;

    private GpxList gpxCache = GpxList.NULL_TRACK;
    private int nodeIndex = -1;

    protected final SolidUnit sunit;
    protected final LabelOverlay ylabel, xlabel;

    protected final UiTheme theme;

    public AbsGraphView(Context context, DispatcherInterface di, UiTheme theme, int... iid) {
        this(context, theme);
        di.addTarget(this, iid);

    }


    public AbsGraphView(Context context, UiTheme theme) {
        super(context);
        this.theme = theme;
        setWillNotDraw(false);
        sunit = new SolidUnit(new Storage(context));

        xlabel = new LabelOverlay(context, Gravity.LEFT | Gravity.BOTTOM);
        ylabel = new LabelOverlay(context, Gravity.RIGHT | Gravity.TOP);

        xlabel.setGravity(Gravity.BOTTOM);
        xlabel.setText(Color.WHITE, R.string.distance, sunit.getDistanceUnit());

        addView(xlabel);
        addView(ylabel);

        setBackgroundColor(theme.getGraphBackgroundColor());
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        onContentUpdated(iid, info, -1);
    }

    public void onContentUpdated(int iid, GpxInformation info, int i) {
        gpxCache = info.getGpxList();
        nodeIndex = i;

        invalidate();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        xlabel.layout(0,0, r-l, b-t);
        ylabel.layout(0,0, r-l, b-t);
    }

    public AbsGraphView hideXLabel() {
        xlabel.setVisibility(GONE);
        return this;
    }

    public boolean isXLabelVisible() {
        return xlabel.getVisibility() == VISIBLE;
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


    @Override
    public void onDraw(Canvas c) {
        if (getWidth() > 0 && getHeight() > 0) {
            boolean markerMode = gpxCache.getMarkerList().size() > getWidth() / SAMPLE_WIDTH_PIXEL;
            plot(c, gpxCache, nodeIndex, sunit, markerMode);
        }
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



    public abstract void plot(Canvas canvas, GpxList list,
                              int index, SolidUnit sunit,
                              boolean markerMode);


    public void setVisibility(GpxInformation info) {
        if (info.isLoaded() && info.getType() == GpxType.ROUTE || info.getType() == GpxType.TRACK) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }
}
