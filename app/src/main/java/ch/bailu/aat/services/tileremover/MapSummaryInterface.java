package ch.bailu.aat.services.tileremover;

public interface MapSummaryInterface {
    boolean isValid();

    String getName();

    StringBuilder buildReport(StringBuilder builder);
}
