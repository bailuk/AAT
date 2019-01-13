package ch.bailu.aat.views.preferences;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTipView;


public class SolidCheckBox extends LinearLayout {

    private final ToolTipView toolTip;

    public SolidCheckBox(final SolidBoolean sboolean) {
        super(sboolean.getContext());
        setOrientation(VERTICAL);
        AppTheme.main.button(this);

        CheckBox checkBox = new CheckBox(getContext());
        AppTheme.main.header(checkBox);

        addView(checkBox);

        toolTip = new ToolTipView(getContext());
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
