package ch.bailu.aat.map.layer.control;

import android.view.View;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.foc.Foc;

public class NodeViewLayer extends AbsNodeViewLayer {


    private Foc file = null;
    private int index = 0;

    private final MapContext mcontext;

    public NodeViewLayer(MapContext mc) {
        super(mc);
        mcontext = mc;
    }


    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (file != null && file.isFile()) {
            startNodeDetailActivity(file.getPath());
        }
    }



    protected void startNodeDetailActivity(String path) {
        NodeDetailActivity.start(mcontext.getContext(), path, index);
    }


    @Override
    public void setSelectedNode(int IID, GpxInformation info, GpxPointNode node, int i) {
        super.setSelectedNode(IID, info, node, i);

        index = i;
        file = info.getFile();

        html.appendInfo(info, index);
        html.appendNode(node, info);
        html.appendAttributes(node.getAttributes());
        setHtmlText(html);
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
