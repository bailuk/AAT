package ch.bailu.aat_lib.html

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class MarkupBuilderTest {

    @Test
    fun testAppendHeader() {
        val markupBuilder = MarkupBuilder(MarkupConfig.HTML)

        markupBuilder.appendHeader("Test")
        assertEquals("<h3>Test</h3>", markupBuilder.toString())

        markupBuilder.clear()
        assertEquals("", markupBuilder.toString())
    }

    @Test
    fun testAppend() {
        val markupBuilder = MarkupBuilder(MarkupConfig.HTML)

        markupBuilder.appendBold("Test")
        assertEquals("<b>Test</b>", markupBuilder.toString())

        markupBuilder.appendNl()
        markupBuilder.append("key1", "value1")
        markupBuilder.appendNl()
        markupBuilder.append("key2", "<a href=\"http://value2\"</a>")
        markupBuilder.appendNl()
        markupBuilder.append("<b>test</b>")
        assertEquals("<b>Test</b><br>key1: value1<br>key2: &lt;a href=&quot;http://value2&quot;&lt;/a&gt;<br>&lt;b&gt;test&lt;/b&gt;", markupBuilder.toString())

    }

    @Test
    fun testAppendBold() {
        val markupBuilder = MarkupBuilder(MarkupConfig.HTML)

        markupBuilder.appendBold("Test")
        assertEquals("<b>Test</b>", markupBuilder.toString())

        markupBuilder.clear()
        assertEquals("", markupBuilder.toString())
    }

    @Test
    fun testAppendKeyValue() {
        val markupBuilder = MarkupBuilder(MarkupConfig.HTML)

        markupBuilder.appendBold("Test")
        assertEquals("<b>Test</b>", markupBuilder.toString())

        markupBuilder.appendNl()
        markupBuilder.appendKeyValue("key1", "value1")
        markupBuilder.appendNl()
        markupBuilder.appendKeyValue("key2", "<a href=\"http://value2\"</a>")
        markupBuilder.appendNl()
        markupBuilder.appendBold("key3", "value3")
        markupBuilder.appendNl()
        assertEquals("<b>Test</b><br>key1=value1<br>key2=&lt;a href=&quot;http://value2&quot;&lt;/a&gt;<br><b>key3=value3</b><br>", markupBuilder.toString())
    }
}
