package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.HtmlScrollTextView;

public abstract class NodeViewLayer extends NodeSelectorLayer implements View.OnLongClickListener {

    private final NodeInfoView infoView;
    private final MapContext mcontext;

    private final Position pos;





    public NodeViewLayer(MapContext cl) {
        super(cl);
        mcontext = cl;

        pos = new Position(cl.getContext());

        infoView = new NodeInfoView(cl.getContext());
        infoView.setOnLongClickListener(this);
        infoView.setVisibility(View.GONE);

        cl.getMapView().addView(infoView);

    }

    public void drawForeground(MapContext cl) {
        super.drawForeground(cl);

        pos.setSize(cl.getMetrics().getWidth(), cl.getMetrics().getHeight());


    }


    public void setGraph(GpxInformation info, int index) {
        infoView.setGraph(info, index);
        measure();
        layout();
        //infoView.invalidate();

    }

    public void setHtmlText(String text) {
        infoView.setHtmlText(text);
        measure();
        layout();
        //infoView.invalidate();
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
        private final int yoffset=0, button_space;

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
            return yoffset;
        }

        public int h() {
            return height;
        }

        public int w() {
            return width;
        }

    }
}
