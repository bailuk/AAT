package ch.bailu.aat_lib.preferences;


import javax.annotation.Nonnull;

public abstract class SolidCheckList extends AbsSolidType {

    public abstract String[] getStringArray();
    public abstract boolean[] getEnabledArray();
    public abstract void setEnabled(int i, boolean isChecked);




    @Override
    public void setValueFromString(String string) {}

    @Nonnull
    @Override
    public String getValueAsString() {
        return "";
    }

}
