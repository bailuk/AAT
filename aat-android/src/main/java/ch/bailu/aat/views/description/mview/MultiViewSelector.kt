package ch.bailu.aat.views.description.mview;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.menus.MultiViewMenu;
import ch.bailu.aat.util.ui.theme.AppTheme;

public class MultiViewSelector extends LinearLayout {
    private final MultiView multiView;

    private final TextView label;

    public MultiViewSelector(MultiView mv) {
        super(mv.getContext());
        multiView = mv;

        label = new TextView(mv.getContext());
        label.setText(mv.getLabel());
        label.setSingleLine();

        AppTheme.bar.header(label);
        AppTheme.bar.button(this);
        addView(label);

        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        setOrientation(HORIZONTAL);
        setOnClickListener(view -> new MultiViewMenu(multiView).showAsPopup(getContext(),
                MultiViewSelector.this));

        multiView.addObserver(() -> label.setText(multiView.getLabel()));
    }
}
