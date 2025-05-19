package ch.bailu.aat_lib.service.tileremover

import ch.bailu.aat_lib.util.MemSize

class SourceSummary(override val name: String) : SourceSummaryInterface {
    private var count = 0

    @JvmField
    var countToRemove = 0
    private var countNew = 0
    var size: Long = 0
    private var sizeToRemove: Long = 0

    @JvmField
    var sizeNew: Long = 0

    fun addFile(length: Long) {
        size += length
        sizeNew += length
        count++
        countNew++
    }

    fun addFileToRemove(length: Long) {
        sizeToRemove += length
        countToRemove++
        sizeNew -= length
        countNew--
    }

    fun addFileRemoved(length: Long) {
        size -= length
        sizeToRemove -= length
        count--
        countToRemove--
    }

    fun clearRm() {
        sizeNew = size
        countNew = count
        sizeToRemove = 0
        countToRemove = 0
    }

    fun clear() {
        size = 0
        count = 0
        clearRm()
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun buildReport(builder: StringBuilder): StringBuilder {
        builder.append(count)
        builder.append('-')
        builder.append(countToRemove)
        builder.append('=')
        builder.append(countNew)
        builder.append('\n')
        MemSize.describe(builder, size.toDouble())
        builder.append('-')
        MemSize.describe(builder, sizeToRemove.toDouble())
        builder.append('=')
        MemSize.describe(builder, sizeNew.toDouble())
        return builder
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SourceSummary

        return name == other.name
    }

}
