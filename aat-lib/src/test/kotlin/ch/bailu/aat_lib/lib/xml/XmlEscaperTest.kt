package ch.bailu.aat_lib.lib.xml

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XmlEscaperTest {

    private fun assertEscaped(expected: String, input: String) {
        assertEquals(expected, XmlEscaper().escape(input))
    }

    @Test
    fun testBasicEscape() {
        assertEscaped(
            "5 &lt; 6 &amp; 7 &gt; 3",
            "5 < 6 & 7 > 3"
        )
    }

    @Test
    fun testQuoteEscape() {
        assertEscaped(
            "&quot;Hello&quot; &apos;World&apos;",
            "\"Hello\" 'World'"
        )
    }

    @Test
    fun testInvalidCharacters() {
        val input = "Valid\u0001Text\u0008With\u001FInvalid"
        val expected = "ValidTextWithInvalid" // invalid characters removed
        assertEscaped(expected, input)
    }

    @Test
    fun testEmptyInput() {
        assertEscaped("", "")
    }

    @Test
    fun testValidUnicode() {
        val input = "Emoji 😊 and Chinese 字"
        assertEscaped(input, input)
    }

    @Test
    fun testMultipleCalls() {
        val input1 = "Just a test containing <"
        val output1 = "Just a test containing &lt;"
        val input2 = "Test containing >"
        val output2 = "Test containing &gt;"

        assertEscaped(output1, input1)
        assertEscaped(output2, input2)
    }


    @Test
    fun testNewLineAndTab() {
        val input = "Tab\tand new\nline\n\rshould disappear"
        assertEscaped("Taband newlineshould disappear", input)
    }
}
