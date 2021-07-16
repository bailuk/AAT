package ch.bailu.aat.views.preferences;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.util.ui.ToolTipView;
import ch.bailu.aat.util.ui.UiTheme;


public class SolidCheckBox extends LinearLayout {

    private final ToolTipView toolTip;

    public SolidCheckBox(final SolidBoolean sboolean, UiTheme theme) {
        super(sboolean.getContext());
        setOrientation(VERTICAL);

        CheckBox checkBox = new CheckBox(getContext());
        theme.header(checkBox);

        addView(checkBox);

        toolTip = new ToolTipView(getContext(), theme);
        toolTip.setToolTip(sboolean);
        addView(toolTip);

        checkBox.setChecked(sboolean.getValue());
        checkBox.setText(sboolean.getLabel());


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sboolean.setValue(isChecked);
            toolTip.setToolTip(sboolean);
        });
    }
}
