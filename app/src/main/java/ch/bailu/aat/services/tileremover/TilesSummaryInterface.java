package ch.bailu.aat.services.tileremover;

public interface TilesSummaryInterface {
    boolean isValid();

    String getName();
    long getSize();
    long getCount();

    StringBuilder buildReport(StringBuilder builder);
}
