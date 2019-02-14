package ch.bailu.aat.util.ui;

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
