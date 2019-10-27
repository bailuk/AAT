package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.view.View;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.HtmlBuilder;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.util.ui.AppLayout;

public abstract class AbsNodeViewLayer extends AbsNodeSelectorLayer implements
        View.OnLongClickListener, View.OnClickListener {

    private final NodeInfoView infoView;
    protected final HtmlBuilderGpx html;

    private final MapContext mcontext;

    private final Position pos;


    public AbsNodeViewLayer(MapContext mc) {
        super(mc);
        mcontext = mc;

        pos = new Position(mc.getContext());

        html = new HtmlBuilderGpx(mc.getContext());

        infoView = new NodeInfoView(mc.getContext());
        infoView.setOnLongClickListener(this);
        infoView.setOnClickListener(this);

        infoView.setVisibility(View.GONE);

        mc.getMapView().addView(infoView);

    }

    @Override
    public void setSelectedNode(int IID, GpxInformation info, GpxPointNode node, int i) {
        infoView.setBackgroundColorFromIID(IID);
        setGraph(info, i);
    }


    public void setGraph(GpxInformation info, int index) {
        infoView.setGraph(info, index);
        measure();
        layout();
    }


    public void setBackgroundColorFromIID(int IID) {
        infoView.setBackgroundColorFromIID(IID);
    }

    public void setHtmlText(HtmlBuilder html) {
        infoView.setHtmlText(html.toString());
        html.clear();
        measure();
        layout();
    }


    public void showAtLeft() {
        pos.toLeft();
        show();
    }


    public void showAtRight() {
        pos.toRight();
        show();
    }


    public void hide() {
        AppLayout.fadeOut(infoView);
        mcontext.getMapView().requestRedraw();
    }


    private void show() {
        measure();
        layout();
        AppLayout.fadeIn(infoView);

        mcontext.getMapView().requestRedraw();

    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        pos.setSize(r-l, b-t);
    }

    private void layout() {
        infoView.layout(
                pos.x(),
                pos.y(),
                pos.x() + pos.w(),
                pos.y() + pos.h());
    }

    private void measure() {
        int wspec = View.MeasureSpec.makeMeasureSpec(pos.w(),
                View.MeasureSpec.EXACTLY);
        int hspec = View.MeasureSpec.makeMeasureSpec(pos.h(),
                View.MeasureSpec.EXACTLY);

        infoView.measure(wspec, hspec);
    }



    private static class Position {
        private int xoffset=0, width, height, right_space;
        private final int button_space;

        public Position(Context c) {
            button_space = AppLayout.getBigButtonSize(c);
        }

        private void setSize(int w, int h) {
            height = Math.min(h / 3, button_space * 3);
            width = Math.min(w - button_space, button_space * 5);

            right_space = w - width - button_space;
        }


        public void toLeft() {
            xoffset = right_space;
        }

        public void toRight() {
            xoffset = button_space;
        }

        public int x() {
            return xoffset;
        }

        public int y() {
            return 0;
        }

        public int h() {
            return height;
        }

        public int w() {
            return width;
        }

    }
}
