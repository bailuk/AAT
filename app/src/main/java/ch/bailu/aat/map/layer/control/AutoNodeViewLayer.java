package ch.bailu.aat.map.layer.control;

import android.view.View;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.util_java.foc.Foc;

public class AutoNodeViewLayer extends NodeViewLayer {

    final HtmlBuilderGpx html;

    private Foc file = null;
    private int index = 0;

    private final MapContext mcontext;

    public AutoNodeViewLayer(MapContext cl) {
        super(cl);
        mcontext = cl;

        html = new HtmlBuilderGpx(cl.getContext());
    }

    @Override
    public boolean onLongClick(View v) {
        if (file != null && file.isFile()) {

            NodeDetailActivity.start(mcontext.getContext(), file.getPath(), index);
            return true;
        }
        return false;
    }


    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int i) {
        index = i;
        file = info.getFile();

        html.clear();

        html.appendInfo(info, index);
        html.appendNode(node, info);
        html.appendAttributes(node.getAttributes());

        setHtmlText(html.toString());
        setGraph(info, i);

    }


    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
