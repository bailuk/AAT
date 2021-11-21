package ch.bailu.aat.views;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.map.mapsforge.MapsForgeViewStatic;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppHtml;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer;

public class NodeEntryView extends LinearLayout {

    private final static UiTheme THEME = AppTheme.search;

    private final MapsForgeViewStatic map;
    private final TextView text;

    private final GpxDynLayer gpxOverlay;


    public NodeEntryView(ServiceContext sc, AbsDispatcher d) {
        super(sc.getContext());
        setOrientation(HORIZONTAL);

        int previewSize = AppLayout.getBigButtonSize(sc.getContext());


        map =  new MapsForgeViewStatic(d.getAppContext(), sc);
        d.addLC(map);

        gpxOverlay = new GpxDynLayer(new Storage(getContext()), map.getMContext(), sc);
        map.add(gpxOverlay);

        text=new TextView(sc.getContext());
        text.setTextColor(Color.WHITE);

        addViewWeight(text);
        addView(map, previewSize, previewSize);

        THEME.content(text);
        THEME.button(this);
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

        final BoundingBoxE6 bounding = node.getBoundingBox();
        map.frameBounding(bounding);
        gpxOverlay.onContentUpdated(iid, info);
    }
}
