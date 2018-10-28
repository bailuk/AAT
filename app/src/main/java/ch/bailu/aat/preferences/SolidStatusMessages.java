package ch.bailu.aat.preferences;

public class SolidStatusMessages extends SolidIndexList {


    public SolidStatusMessages(Storage s, String k) {
        super(s, k);
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    protected String getValueAsString(int i) {
        return null;
    }
}
