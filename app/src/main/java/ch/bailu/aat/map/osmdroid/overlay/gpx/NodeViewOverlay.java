package ch.bailu.aat.map.osmdroid.overlay.gpx;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnLongClickListener;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.map.osmdroid.OsmInteractiveView;


public abstract class NodeViewOverlay extends NodeSelectorOverlay implements OnLongClickListener {
    private static final int XMARGIN=0;
    private static final int YMARGIN=0;

    private final int big_margin;

    private final HtmlScrollTextView infoView;

    private int xoffset, yoffset;


    public NodeViewOverlay(OsmInteractiveView v) {
        super(v);
        big_margin = AppTheme.getBigButtonSize(getContext()) + XMARGIN;

        infoView = new HtmlScrollTextView(getContext());
        infoView.setBackgroundColor(Color.argb(0xcc, 0xff, 0xff, 0xff));
        infoView.getTextView().setTextColor(Color.BLACK);
        infoView.getTextView().setOnLongClickListener(this);
        infoView.setVisibility(View.GONE);

        v.addView(infoView);

    }



    public void setHtmlText(String text) {
        infoView.setHtmlText(text);

        measure();
        layout();
        infoView.invalidate();
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
        getOsmView().requestRedraw();
    }


    private void show() {
        measure();
        layout();
        infoView.setVisibility(View.VISIBLE);

        getOsmView().requestRedraw();

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
        return getOsmView().getHeight() / 3;
    }

    private int getWidth() {
        return getOsmView().getWidth() - big_margin;
    }
}
