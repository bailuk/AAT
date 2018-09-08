package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.PercentageLayout;

public class GraphViewContainer extends ViewGroup implements View.OnClickListener {
    public final static int TRANSPARENT = Color.argb(200, 0, 0, 0);

    private final PercentageLayout canvas;

    private final ControlBar bar;

    private GraphViewContainer(Context context) {
        super(context);


        canvas = new PercentageLayout(context);
        canvas.setOrientation(LinearLayout.VERTICAL);

        addView(canvas);

        bar = new ControlBar(context, LinearLayout.HORIZONTAL);
        bar.setVisibility(GONE);
        bar.setBackgroundColor(TRANSPARENT);
        addView(bar);

    }


    public static GraphViewContainer speed(Context c, String key, DispatcherInterface di, int... iid) {
        GraphViewContainer v =new GraphViewContainer(c).
                add(new DistanceSpeedGraphView(c, key, di , iid), 100);

        v.bar.addIgnoreSize(new SpeedLegendView(c, key));
        return v;
    }


    public static GraphViewContainer speedAltitude(Context c, String key, DispatcherInterface di, int... iid) {
        GraphViewContainer v =new GraphViewContainer(c).
                add(new DistanceAltitudeGraphView(c, di, iid), 50).
                add(new DistanceSpeedGraphView(c, key, di , iid), 50);

        v.bar.addIgnoreSize(new SpeedLegendView(c, key));
        return v;
    }


    private GraphViewContainer add(AbsGraphView v, int p) {
        v.setDrawingCacheEnabled(true);
        v.setOnClickListener(this);
        canvas.add(v, p);
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int parent_height=b-t;
        int parent_width =r-l;

        canvas.layout(0, 0, parent_width, parent_height);
        bar.place(0,parent_height-bar.getControlSize(), parent_width);
    }


    @Override
    public void onClick(View v) {
        if (bar.getVisibility()==VISIBLE) {
            AppLayout.fadeOut(bar);
        } else {
            AppLayout.fadeIn(bar);
        }
    }
}
