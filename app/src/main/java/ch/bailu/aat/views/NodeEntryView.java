package ch.bailu.aat.views;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.helpers.AppHtml;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.helpers.HtmlBuilderGpx;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.CachedTileProvider;
import ch.bailu.aat.views.map.MapDensity;
import ch.bailu.aat.views.map.OsmViewStatic;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;

public class NodeEntryView extends LinearLayout {

    private final OsmViewStatic map;
    private final TextView text;

    private final GpxDynOverlay gpxOverlay;

    private final int previewSize;

    private static int count=0;
    private int id;

    public NodeEntryView(ServiceContext sc) {
        super(sc.getContext());
        setOrientation(HORIZONTAL);

        id = count;
        count++;
        previewSize = AppTheme.getBigButtonSize(sc.getContext());


        map = new OsmViewStatic(sc.getContext(),
                new CachedTileProvider(sc),
                new MapDensity(sc.getContext()));
        gpxOverlay = new GpxDynOverlay(map, sc ,-1);
        map.add(gpxOverlay);

        text=new TextView(sc.getContext());
        text.setTextColor(Color.WHITE);

        addViewWeight(text);
        addView(map, previewSize, previewSize);

        AppLog.d(this, "construct " + id);
    }


    private void addViewWeight(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        AppLog.d(this, "onAttachedToWindow " + id);
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AppLog.d(this, "onDetachedFromWindow " + id);
    }


    public void update(GpxInformation info, GpxPointNode node) {
        HtmlBuilderGpx html = new HtmlBuilderGpx(getContext());
        html.appendNode(node, info);
        html.appendAttributes(node.getAttributes());

        text.setText(AppHtml.fromHtml(html.toString()));

        final BoundingBox bounding = node.getBoundingBox();
        map.frameBoundingBox(bounding);
        gpxOverlay.onContentUpdated(info);

        AppLog.d(this, "update " + id);
    }
}
