package ch.bailu.aat_lib.lib.xml

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XmlEscaperTest {

    @Test
    fun testBasicEscape() {
        assertEquals(
            "5 &lt; 6 &amp; 7 &gt; 3",
            XmlEscaper().escape("5 < 6 & 7 > 3")
        )
    }

    @Test
    fun testQuoteEscape() {
        assertEquals(
            "&quot;Hello&quot; &apos;World&apos;",
            XmlEscaper().escape("\"Hello\" 'World'")
        )
    }

    @Test
    fun testInvalidCharacters() {
        val input = "Valid\u0001Text\u0008With\u001FInvalid"
        val expected = "ValidTextWithInvalid" // invalid characters removed
        assertEquals(expected, XmlEscaper().escape(input))
    }

    @Test
    fun testEmptyInput() {
        assertEquals("", XmlEscaper().escape(""))
    }

    @Test
    fun testValidUnicode() {
        val input = "Emoji 😊 and Chinese 字"
        assertEquals(input, XmlEscaper().escape(input))
    }

    @Test
    fun testMultipleCalls() {
        val input1 = "Just a test containing <"
        val output1 = "Just a test containing &lt;"
        val input2 = "Test containing >"
        val output2 = "Test containing &gt;"

        val escaper = XmlEscaper()

        assertEquals(output1, escaper.escape(input1))
        assertEquals(output2, escaper.escape(input2))
    }
}
