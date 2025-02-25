package ch.bailu.aat_lib.service.tileremover

interface SourceSummaryInterface {
    val name: String
    fun buildReport(builder: StringBuilder): StringBuilder
}
