package ch.bailu.aat.map.layer.control;

import android.content.SharedPreferences;
import android.view.View;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import java.io.File;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.HtmlBuilderGpx;

public class AutoNodeViewLayer extends NodeViewLayer {

    final HtmlBuilderGpx html;

    private String fileID = null;
    private int index = 0;

    private final MapContext mcontext;

    public AutoNodeViewLayer(MapContext cl) {
        super(cl);
        mcontext = cl;

        html = new HtmlBuilderGpx(cl.getContext());
    }

    @Override
    public boolean onLongClick(View v) {
        if (fileID != null && new File(fileID).isFile()) {

            NodeDetailActivity.start(mcontext.getContext(), fileID, index);
            return true;
        }
        return false;
    }


    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int i) {
        index = i;
        fileID = info.getPath();

        html.clear();

        html.appendInfo(info, index);
        html.appendNode(node, info);
        html.appendAttributes(node.getAttributes());

        setHtmlText(html.toString());

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
