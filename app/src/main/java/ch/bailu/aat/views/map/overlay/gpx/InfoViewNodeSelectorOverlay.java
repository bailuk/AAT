package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.helpers.AppHtml;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.views.map.OsmInteractiveView;


public class InfoViewNodeSelectorOverlay extends NodeSelectorOverlay implements OnLongClickListener {
    private static final int XMARGIN=10;
    private static final int YMARGIN=35;

    private final int big_margin;

    private String fileID = null;
    private int index = 0;

    private final TextView infoView;


    public InfoViewNodeSelectorOverlay(OsmInteractiveView v, int id) {
        super(v, id);
        big_margin = AppTheme.getBigButtonSize(getContext()) + XMARGIN;

        infoView = new TextView(getContext());
        infoView.setBackgroundColor(Color.argb(0xcc, 0xff, 0xff, 0xff));
        infoView.setTextColor(Color.BLACK);
        infoView.setOnLongClickListener(this);

        v.addView(infoView);

    }


    @Override
    public boolean onLongClick(View v) {
        if (fileID != null) {
            NodeDetailActivity.start(getContext(), fileID, index);
        }
        return true;
    }




    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int i) {
        StringBuilder builder = new StringBuilder();

        node.toHtml(getContext(), builder);
        infoView.setText(AppHtml.fromHtml(builder.toString()));

        fileID=info.getPath();
        index=i;
    }


    public void showAtLeft() {
        infoView.layout(XMARGIN, YMARGIN, 
                getOsmView().getWidth() - big_margin,
                getOsmView().getHeight() / 3);

        infoView.setVisibility(View.VISIBLE);
    }


    public void showAtRight() {
        infoView.layout(big_margin, YMARGIN, 
                getOsmView().getWidth() - XMARGIN,
                getOsmView().getHeight() / 3);
        infoView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        infoView.setVisibility(View.INVISIBLE);
    }
}
