package ch.bailu.aat.map.mapsforge.layer.control;

import android.content.SharedPreferences;
import android.view.View;

import java.io.File;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;

public class AutoNodeViewLayer extends NodeViewLayer {

    final HtmlBuilderGpx html;

    private String fileID = null;
    private int index = 0;

    private final MapContext clayer;

    public AutoNodeViewLayer(MapContext cl) {
        super(cl);
        clayer = cl;

        html = new HtmlBuilderGpx(cl.context);
    }

    @Override
    public boolean onLongClick(View v) {
        if (fileID != null && new File(fileID).isFile()) {

            NodeDetailActivity.start(clayer.context, fileID, index);
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
}
