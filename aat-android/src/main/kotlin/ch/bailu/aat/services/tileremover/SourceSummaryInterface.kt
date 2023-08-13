package ch.bailu.aat.services.tileremover

interface SourceSummaryInterface {
    val name: String
    fun buildReport(builder: StringBuilder): StringBuilder
}
