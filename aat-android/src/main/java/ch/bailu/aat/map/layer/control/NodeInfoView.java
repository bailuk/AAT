package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.graph.GraphView;
import ch.bailu.aat.views.graph.GraphViewFactory;
import ch.bailu.aat.views.html.HtmlScrollTextView;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapColor;


public final class NodeInfoView extends PercentageLayout {
    private final HtmlScrollTextView htmlView;
    private final GraphView graphView;
    private final GraphView limitGraphView;


    private int backgroundColor;

    public NodeInfoView(AppContext appContext, Context context) {
        super(context,0);
        setOrientation(LinearLayout.VERTICAL);


        backgroundColor = MapColor.LIGHT;

        htmlView = new HtmlScrollTextView(context);
        htmlView.getTextView().setTextColor(MapColor.TEXT);
        htmlView.setBackgroundColor(backgroundColor);
        add(htmlView, 50);



        graphView = createGraphView(appContext, context);
        add(graphView, 25);

        limitGraphView = createGraphView(appContext, context);
        add(limitGraphView, 25);

        setBackgroundColor(Color.TRANSPARENT);
    }

    private GraphView createGraphView(AppContext appContext, Context context) {
        GraphView g = GraphViewFactory.createAltitudeGraph(appContext, context, AppTheme.bar);
        g.setVisibility(GONE);
        g.setBackgroundColor(MapColor.DARK);
        g.showLabel(false);
        return g;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        htmlView.setClickable(true);
        htmlView.setOnClickListener(l);
        super.setOnClickListener(l);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        htmlView.getTextView().setOnLongClickListener(l);
        super.setOnLongClickListener(l);
    }



    public void setBackgroundColorFromIID(int IID) {
        int newBackgroundColor = MapColor.getColorFromIID(IID);

        if (backgroundColor != newBackgroundColor) {
            backgroundColor = newBackgroundColor;
            htmlView.setBackgroundColor(toBackgroundColorLight(backgroundColor));
            graphView.setBackgroundColor(toBackgroundColorDark(backgroundColor));
            limitGraphView.setBackgroundColor(toBackgroundColorDark(backgroundColor));
        }
    }

    public void setHtmlText(String htmlText) {
        htmlView.setHtmlText(htmlText);
    }

    private int toBackgroundColorLight(int color) {
        return MapColor.toLightTransparent(color);

    }


    private int toBackgroundColorDark(int color) {
        return MapColor.toDarkTransparent(color);
    }

    public void setGraph(GpxInformation info, int index, int firstPoint, int lastPoint) {
        graphView.setVisibility(info);
        graphView.onContentUpdated(InfoID.ALL, info, index);

        limitGraphView.setVisibility(info);
        limitGraphView.setLimit(firstPoint, lastPoint);
        limitGraphView.onContentUpdated(InfoID.ALL, info, index);
    }
}
