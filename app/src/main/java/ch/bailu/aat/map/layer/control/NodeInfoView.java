package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.views.html.HtmlScrollTextView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;


public class NodeInfoView extends PercentageLayout {
    private final HtmlScrollTextView htmlView;
    private final DistanceAltitudeGraphView graphView;


    public NodeInfoView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        htmlView = new HtmlScrollTextView(context);
        htmlView.getTextView().setTextColor(MapColor.TEXT);
        htmlView.setBackgroundColor(MapColor.LIGHT);
        add(htmlView, 60);


        graphView = new DistanceAltitudeGraphView(context);
        graphView.setVisibility(GONE);
        graphView.setBackgroundColor(MapColor.DARK);
        graphView.showLabel(false);
        add(graphView, 40);

        setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        htmlView.getTextView().setOnLongClickListener(l);
        super.setOnLongClickListener(l);
    }


    public void setHtmlText(String htmlText) {
        htmlView.setHtmlText(htmlText);
    }

    public void setGraph(GpxInformation info, int index) {
        graphView.setVisibility(info);
        graphView.onContentUpdated(InfoID.ALL, info, index);
    }
}
