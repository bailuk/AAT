package ch.bailu.aat.map.layer.control;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.HtmlScrollTextView;

public abstract class NodeViewLayer extends NodeSelectorLayer implements View.OnLongClickListener {
    private static final int XMARGIN=0;
    private static final int YMARGIN=0;

    private final int big_margin;

    private final HtmlScrollTextView infoView;

    private int xoffset, yoffset;

    private final MapContext mcontext;

    private class DrawToUIHandler extends Handler{
        private String text=null;

        @Override
        public void handleMessage(Message m) {
            dispatchText();
        }

        private synchronized void dispatchText() {
            if (text != null){
                infoView.setHtmlText(text);
                measure();
                layout();
                infoView.invalidate();
            }
            text = null;
        }

        public synchronized void setText(String t) {
            text=t;
        }
    };

    private final DrawToUIHandler drawToUI = new DrawToUIHandler();


    public NodeViewLayer(MapContext cl) {
        super(cl);
        mcontext = cl;
        big_margin = AppTheme.getBigButtonSize(cl.getContext()) + XMARGIN;

        infoView = new HtmlScrollTextView(cl.getContext());
        infoView.setBackgroundColor(Color.argb(0xcc, 0xff, 0xff, 0xff));
        infoView.getTextView().setTextColor(Color.BLACK);
        infoView.getTextView().setOnLongClickListener(this);
        infoView.setVisibility(View.GONE);

        cl.getMapView().addView(infoView);

    }



    public void setHtmlText(String text) {
        drawToUI.setText(text);
        drawToUI.sendEmptyMessage(1);
    }

    public void showAtLeft() {
        toLeft();
        show();
    }


    public void showAtRight() {
        toRight();
        show();
    }


    public void hide() {
        infoView.setVisibility(View.GONE);
        mcontext.getMapView().requestRedraw();
    }


    private void show() {
        measure();
        layout();
        infoView.setVisibility(View.VISIBLE);

        mcontext.getMapView().requestRedraw();

    }


    private void layout() {
        infoView.layout(
                xoffset,
                yoffset,
                xoffset+getWidth(),
                yoffset+getHeight());
    }

    private void measure() {
        int wspec = View.MeasureSpec.makeMeasureSpec(getWidth(),
                View.MeasureSpec.EXACTLY);
        int hspec = View.MeasureSpec.makeMeasureSpec(getHeight(),
                View.MeasureSpec.EXACTLY);

        infoView.measure(wspec, hspec);
    }



    private void toLeft() {
        xoffset=XMARGIN;
        yoffset=YMARGIN;
    }

    public void toRight() {
        xoffset=big_margin;
        yoffset=YMARGIN;

    }

    private int getHeight() {
        return mcontext.getMetrics().getHeight() / 3;
    }

    private int getWidth() {
        return mcontext.getMetrics().getWidth() - big_margin;
    }
}
