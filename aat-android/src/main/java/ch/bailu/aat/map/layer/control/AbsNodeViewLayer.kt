package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.view.View;

import javax.annotation.Nonnull;

import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.html.MarkupBuilder;
import ch.bailu.aat_lib.html.MarkupBuilderGpx;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.edge.Position;
import ch.bailu.aat_lib.map.layer.gpx.GpxVisibleLimit;
import ch.bailu.aat_lib.map.layer.selector.AbsNodeSelectorLayer;

public abstract class AbsNodeViewLayer extends AbsNodeSelectorLayer implements
        View.OnLongClickListener, View.OnClickListener {

    private final NodeInfoView infoView;
    protected final MarkupBuilderGpx markupBuilder;
    private final MapContext mcontext;
    private final Placer pos;

    public AbsNodeViewLayer(AppContext appContext, Context context, MapContext mc) {
        super(appContext.getServices(), appContext.getStorage(), mc, Position.BOTTOM);
        mcontext = mc;

        pos = new Placer(context);

        markupBuilder = new MarkupBuilderGpx(appContext.getStorage());

        infoView = new NodeInfoView(appContext, context);
        infoView.setOnLongClickListener(this);
        infoView.setOnClickListener(this);
        infoView.setVisibility(View.GONE);

        To.view(mc.getMapView()).addView(infoView);

    }

    @Override
    public void setSelectedNode(int IID, @Nonnull GpxInformation info, @Nonnull GpxPointNode node, int i) {
        infoView.setBackgroundColorFromIID(IID);

        GpxVisibleLimit limit = new GpxVisibleLimit(mcontext);
        limit.walkTrack(info.getGpxList());

        setGraph(info, i, limit.getFirstPoint(), limit.getLastPoint());
    }

    public void setGraph(GpxInformation info, int index, int firstPoint, int lastPoint) {
        infoView.setGraph(info, index, firstPoint, lastPoint);
        measure();
        layout();
    }

    public void setBackgroundColorFromIID(int IID) {
        infoView.setBackgroundColorFromIID(IID);
    }

    public void setHtmlText(MarkupBuilder html) {
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

    public void show() {
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

    private static class Placer {
        private int xoffset=0, width, height, right_space;
        private final int button_space;

        public Placer(Context c) {
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
