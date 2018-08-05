package ch.bailu.aat.map.layer.control;

import android.view.View;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.util_java.foc.Foc;

public class GeneralNodeViewLayer extends AbsNodeViewLayer {


    private Foc file = null;
    private int index = 0;

    private final MapContext mcontext;

    public GeneralNodeViewLayer(MapContext cl) {
        super(cl);
        mcontext = cl;
    }


    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (file != null && file.isFile()) {

            NodeDetailActivity.start(mcontext.getContext(), file.getPath(), index);
        }
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
