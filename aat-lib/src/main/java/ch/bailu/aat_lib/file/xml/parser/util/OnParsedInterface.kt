package ch.bailu.aat_lib.file.xml.parser.util

import java.io.IOException

interface OnParsedInterface {
    fun onHaveSegment()

    @Throws(IOException::class)
    fun onHavePoint()

    companion object {
        @JvmField
		val NULL: OnParsedInterface = object : OnParsedInterface {
            override fun onHaveSegment() {}
            override fun onHavePoint() {}
        }
    }
}
