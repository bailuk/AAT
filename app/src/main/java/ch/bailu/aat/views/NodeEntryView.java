package ch.bailu.aat.views;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.helpers.AppHtml;
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

    public NodeEntryView(ServiceContext sc) {
        super(sc.getContext());
        setOrientation(HORIZONTAL);

        int previewSize = AppTheme.getBigButtonSize(sc.getContext());


        map = new OsmViewStatic(sc.getContext(),
                new CachedTileProvider(sc),
                new MapDensity(sc.getContext()));
        gpxOverlay = new GpxDynOverlay(map, sc ,-1);
        map.add(gpxOverlay);

        text=new TextView(sc.getContext());
        text.setTextColor(Color.WHITE);

        addViewWeight(text);
        addView(map, previewSize, previewSize);
    }


    private void addViewWeight(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }



    public void update(int iid, GpxInformation info, GpxPointNode node) {
        HtmlBuilderGpx html = new HtmlBuilderGpx(getContext());
        html.appendNode(node, info);
        html.appendAttributes(node.getAttributes());

        text.setText(AppHtml.fromHtml(html.toString()));

        final BoundingBox bounding = node.getBoundingBox();
        map.frameBoundingBox(bounding);
        gpxOverlay.onContentUpdated(iid, info);
    }
}
