package ch.bailu.aat.views;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.helpers.AppHtml;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.AbsOsmTileProvider;
import ch.bailu.aat.views.map.CachedTileProvider;
import ch.bailu.aat.views.map.MapDensity;
import ch.bailu.aat.views.map.OsmViewStatic;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;

public class NodeEntryView extends ViewGroup {

    private final OsmViewStatic map;
    private final TextView text;

    private final int previewSize;

    public NodeEntryView(ServiceContext sc, int id) {
        super(sc.getContext());

        previewSize = AppTheme.getBigButtonSize(sc.getContext());

        AbsOsmTileProvider provider = new CachedTileProvider(sc);
        map = new OsmViewStatic(sc.getContext(), provider, new MapDensity(sc.getContext()));
        
        map.add(new GpxDynOverlay(map, sc ,id));

        text=new TextView(sc.getContext());
        text.setTextColor(Color.WHITE);

        addView(map);
        addView(text);
    }


    @Override
    protected void onMeasure(int wspec, int hspec) {
        final int width  = MeasureSpec.getSize(wspec);
        final int height = previewSize;

        final int wspecText = MeasureSpec.makeMeasureSpec(
                width-previewSize, MeasureSpec.EXACTLY);

        hspec = MeasureSpec.makeMeasureSpec(previewSize, MeasureSpec.EXACTLY);

        map.measure(hspec, hspec);
        text.measure(wspecText, hspec);

        //super.onMeasure(wspec, hspec);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        text.layout(0, 0, text.getMeasuredWidth(), text.getMeasuredHeight());
        map.layout(text.getMeasuredWidth(), 0, text.getMeasuredWidth()+map.getMeasuredHeight(), map.getMeasuredHeight());
        
    }

    
    public void update(GpxInformation info, GpxPointNode node) {
        text.setText(AppHtml.fromHtml(node.toHtml(getContext(), new StringBuilder()).toString()));
        final BoundingBox bounding = node.getBoundingBox();
        map.frameBoundingBox(bounding);
        // TODO send update to gpx overlay .onContentUpdated(info);

    }
}
