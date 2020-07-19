package ch.bailu.aat.views.description.mview;

import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;

public class MultiViewIndicator extends ViewGroup {
    private final static UiTheme THEME = AppTheme.bar;

    private final MultiView multiView;
    private final View indicatorView;

    private final static int HEIGHT = 5;
    private int width = 0;

    public MultiViewIndicator(MultiView multiView) {
        super(multiView.getContext());
        this.multiView = multiView;

        multiView.addObserver(() -> layoutIndicator());

        setBackgroundColor(0);
        setClickable(false);

        indicatorView = new View(getContext());
        indicatorView.setBackgroundColor(THEME.getHighlightColor());
        indicatorView.setClickable(false);
        addView(indicatorView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        width = r-l;
        layoutIndicator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void layoutIndicator() {
        int active = multiView.getActive();
        int count = multiView.pageCount();

        if (count > 0) {
            int w = width / count;
            int x = w*active;
            indicatorView.layout(x, 0, x+w, HEIGHT);
        }
    }
}
