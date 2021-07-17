package ch.bailu.aat.util.ui;

import ch.bailu.aat_lib.util.ui.ToolTipProvider;

public class ToolTipString implements ToolTipProvider {

    private final String toolTip;

    public ToolTipString(String tt) {
        toolTip = tt;
    }

    @Override
    public String getToolTip() {
        return toolTip;
    }
}
