package ch.bailu.aat.services.tileremover;

public interface SourceSummaryInterface {
    boolean isValid();

    String getName();

    StringBuilder buildReport(StringBuilder builder);
}
