package ch.bailu.aat.views.preferences;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.preferences.SolidBoolean;


public class SolidCheckBox extends CheckBox {

    public SolidCheckBox(final SolidBoolean sboolean) {
        super(sboolean.getContext());

        AppTheme.themify(this);
        setChecked(sboolean.getValue());
        setText(sboolean.getLabel());
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sboolean.setValue(isChecked);
            }
        });
    }
}
