package ch.bailu.aat.preferences

import ch.bailu.foc.FocFile
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test


class SolidExportedDocumentTest {

    @Test
    fun testSetValueFromString() {
        val mockStorage = MockStorage()

        val exportedDocument = SolidExportedDocument(mockStorage)

        assertEquals(false, exportedDocument.isExportAllowed(testFile1))

        exportedDocument.setValueFromString(testFile1)

        assertEquals(true, exportedDocument.isExportAllowed(testFile1))
        assertEquals(false, exportedDocument.isExportAllowed(testFileEmpty))
        assertEquals(false, exportedDocument.isExportAllowed(testFile2))

        assertTrue(mockStorage.mockLongValue > 0)
        mockStorage.mockLongValue -= LIMIT_MILLIS

        assertEquals(false, exportedDocument.isExportAllowed(testFile1))
    }


    @Test
    fun testSetValue() {
        val mockStorage = MockStorage()

        val exportedDocument = SolidExportedDocument(mockStorage)

        assertEquals(false, exportedDocument.isExportAllowed(testFile1))

        exportedDocument.setValue(testFile1)

        assertEquals(true, exportedDocument.isExportAllowed(testFile1))
        assertEquals(false, exportedDocument.isExportAllowed(testFileEmpty))
        assertEquals(false, exportedDocument.isExportAllowed(testFile2))

        assertTrue(mockStorage.mockLongValue > 0)
        mockStorage.mockLongValue -= LIMIT_MILLIS

        assertEquals(false, exportedDocument.isExportAllowed(testFile1))
    }

    @Test
    fun testSetDocument() {
        val mockStorage = MockStorage()

        val exportedDocument = SolidExportedDocument(mockStorage)

        assertEquals(false, exportedDocument.isExportAllowed(testFile1))

        exportedDocument.setDocument(FocFile(testFile1))

        assertEquals(true, exportedDocument.isExportAllowed(testFile1))
        assertEquals(false, exportedDocument.isExportAllowed(testFileEmpty))
        assertEquals(false, exportedDocument.isExportAllowed(testFile2))

        assertTrue(mockStorage.mockLongValue > 0)
        mockStorage.mockLongValue -= LIMIT_MILLIS

        assertEquals(false, exportedDocument.isExportAllowed(testFile1))
    }


    @Test
    fun testEmpty() {
        val mockStorage = MockStorage()

        val exportedDocument = SolidExportedDocument(mockStorage)

        assertEquals(false, exportedDocument.isExportAllowed(testFileEmpty))

        mockStorage.mockLongValue = System.currentTimeMillis() + LIMIT_MILLIS
        assertEquals(false, exportedDocument.isExportAllowed(testFile1))

        exportedDocument.setValueFromString(testFileEmpty)
        assertEquals(true, exportedDocument.isExportAllowed(testFileEmpty))
        assertEquals(false, exportedDocument.isExportAllowed(testFile1))
        assertEquals(false, exportedDocument.isExportAllowed(testFile2))

        assertTrue(mockStorage.mockLongValue > 0)
        mockStorage.mockLongValue -= LIMIT_MILLIS

        assertEquals(false, exportedDocument.isExportAllowed(testFileEmpty))
    }

    companion object {
        const val testFile1 = "/storage/path/file.gpx"
        const val testFile2 = "/storage/path/file1.gpx"
        const val testFileEmpty = ""
        const val LIMIT_MILLIS = 30 * 1000 // 1/2 minute
    }
}
