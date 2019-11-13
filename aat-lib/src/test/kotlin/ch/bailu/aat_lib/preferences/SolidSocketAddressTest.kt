package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.mock.MockStorage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.net.InetSocketAddress

class SolidSocketAddressTest {

    @Test
    fun testDefaultPortParsing() {
        val storage = MockStorage()
        val socketAddress = SolidSocketAddress(storage, "test_key", 5598)

        // Test IPv4 without port (should default to 5598)
        storage.writeString("test_key", "127.0.0.1")
        val result1 = socketAddress.getValue() as InetSocketAddress
        assertEquals("127.0.0.1", result1.hostString)
        assertEquals(5598, result1.port)

        // Test hostname without port (should default to 5598)
        storage.writeString("test_key", "localhost")
        val result2 = socketAddress.getValue() as InetSocketAddress
        assertEquals("localhost", result2.hostString)
        assertEquals(5598, result2.port)

        // Test IPv6 without port (should default to 5598)
        storage.writeString("test_key", "[::1]")
        val result3 = socketAddress.getValue() as InetSocketAddress
        assertEquals("0:0:0:0:0:0:0:1", result3.hostString)
        assertEquals(5598, result3.port)

        // Test IPv6 with brackets and no port (should default to 5598)
        storage.writeString("test_key", "[2001:db8::1]")
        val result4 = socketAddress.getValue() as InetSocketAddress
        assertEquals("2001:db8:0:0:0:0:0:1", result4.hostString)
        assertEquals(5598, result4.port)
    }

    @Test
    fun testExplicitPortParsing() {
        val storage = MockStorage()
        val socketAddress = SolidSocketAddress(storage, "test_key", 5598)

        // Test IPv4 with explicit port
        storage.writeString("test_key", "127.0.0.1:8080")
        val result1 = socketAddress.getValue() as InetSocketAddress
        assertEquals("127.0.0.1", result1.hostString)
        assertEquals(8080, result1.port)

        // Test IPv6 with explicit port
        storage.writeString("test_key", "[::1]:8080")
        val result2 = socketAddress.getValue() as InetSocketAddress
        assertEquals("0:0:0:0:0:0:0:1", result2.hostString)
        assertEquals(8080, result2.port)
    }

    @Test
    fun testFormatting() {
        val storage = MockStorage()
        val socketAddress = SolidSocketAddress(storage, "test_key", 5598)

        // Test IPv4 with default port (should omit port)
        socketAddress.setValue(InetSocketAddress("127.0.0.1", 5598))
        assertEquals("127.0.0.1", storage.readString("test_key"))

        // Test IPv4 with custom port (should include port)
        socketAddress.setValue(InetSocketAddress("127.0.0.1", 8080))
        assertEquals("127.0.0.1:8080", storage.readString("test_key"))

        // Test IPv6 with default port (should omit port and brackets)
        socketAddress.setValue(InetSocketAddress("::1", 5598))
        System.err.println(storage.readString("test_key"))
        assertEquals("::1", storage.readString("test_key"))

        // Test IPv6 with custom port (should include port)
        socketAddress.setValue(InetSocketAddress("::1", 8080))
        assertEquals("[::1]:8080", storage.readString("test_key"))
    }

    @Test
    fun testValidation() {
        val storage = MockStorage()
        val socketAddress = SolidSocketAddress(storage, "test_key", 5598)

        // Valid addresses
        assertTrue(socketAddress.validate("127.0.0.1"))
        assertTrue(socketAddress.validate("localhost"))
        assertTrue(socketAddress.validate("[::1]"))
        assertTrue(socketAddress.validate("127.0.0.1:8080"))
        assertTrue(socketAddress.validate("[::1]:8080"))
        assertTrue(socketAddress.validate(""))  // Empty is valid (uses default)

        // Invalid addresses
        assertFalse(socketAddress.validate("127.0.0.1:99999"))  // Port too high
        assertFalse(socketAddress.validate("[::1]:0"))          // Port too low
        assertFalse(socketAddress.validate("[::1"))            // Missing closing bracket
    }

    @Test
    fun testDifferentDefaultPorts() {
        val storage = MockStorage()
        val socketAddress1234 = SolidSocketAddress(storage, "test_key", 1234)

        // Test with different default port
        storage.writeString("test_key", "localhost")
        val result = socketAddress1234.getValue() as InetSocketAddress
        assertEquals("localhost", result.hostString)
        assertEquals(1234, result.port)

        // Test formatting with different default port
        socketAddress1234.setValue(InetSocketAddress("127.0.0.1", 1234))
        assertEquals("127.0.0.1", storage.readString("test_key"))  // Should omit port

        socketAddress1234.setValue(InetSocketAddress("127.0.0.1", 5598))
        assertEquals("127.0.0.1:5598", storage.readString("test_key"))  // Should include port
    }
}
