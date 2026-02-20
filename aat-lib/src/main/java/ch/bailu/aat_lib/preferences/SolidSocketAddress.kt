package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.exception.ValidationException
import com.google.common.net.InetAddresses
import java.net.InetSocketAddress
import java.net.SocketAddress

open class SolidSocketAddress(
    storage: StorageInterface,
    key: String,
    private val defaultPort: Int
) : SolidString(storage, key) {

    open fun getValue(): SocketAddress? {
        val addressString = super.getValueAsString()
        if (getStorage().isDefaultString(addressString)) {
            return getDefaultValue()
        }
        return parseAddress(addressString)
    }

    fun setValue(address: SocketAddress) {
        val addressString = formatAddress(address, defaultPort)
        setValue(addressString)
    }

    override fun getValueAsString(): String {
        val address = getValue() ?: getDefaultValue()
        return if (address == null) {
            ""
        } else {
            formatAddress(address, defaultPort)
        }
    }

    override fun setValueFromString(string: String) {
        val trimmed = string.trim()
        if (trimmed.isEmpty()) {
            setValue(getStorage().getDefaultString())
            return
        }

        val address = parseAddress(trimmed)
            ?: throw ValidationException("Invalid address format")
        setValue(address)
    }

    override fun validate(s: String): Boolean {
        val trimmed = s.trim()
        if (trimmed.isEmpty()) return true
        return parseAddress(trimmed) != null
    }

    /**
     * Override this to provide a default address when none is configured
     */
    open fun getDefaultValue(): SocketAddress? {
        return null
    }

    /**
     * List of possible values to select from (for UI dropdowns/selection lists)
     */
    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        getDefaultValue()?.let { defaultAddress ->
            list.add(formatAddress(defaultAddress, defaultPort))
        }
        return list
    }

    /**
     * Parse a string to a SocketAddress.
     */
    private fun parseAddress(addressString: String): SocketAddress? {
        if (addressString.isEmpty()) {
            return null
        }

        try {
            // Handle IPv6 with brackets: [::1]:5598 or [::1]
            if (addressString.startsWith("[")) {
                val closeBracket = addressString.indexOf(']')
                if (closeBracket == -1) {
                    return null
                }
                val host = addressString.substring(1, closeBracket)

                // Check if there's a port after the closing bracket
                val remainingPart = addressString.substring(closeBracket + 1)
                val port = if (remainingPart.isEmpty()) {
                    // No port specified, use default
                    defaultPort
                } else if (remainingPart.startsWith(":")) {
                    // Port specified after colon
                    val portStr = remainingPart.substring(1)
                    portStr.toIntOrNull() ?: return null
                } else {
                    // Invalid format after closing bracket
                    return null
                }

                if (port < 1 || port > 65535) return null
                return InetSocketAddress(host, port)
            }

            // Handle IPv4 or simple hostnames: host:port or host
            val lastColon = addressString.lastIndexOf(':')

            val host: String
            val port: Int

            if (lastColon == -1) {
                // No port specified, use default
                host = addressString.trim()
                port = defaultPort
            } else {
                // Port specified
                host = addressString.substring(0, lastColon).trim()
                val portStr = addressString.substring(lastColon + 1).trim()
                port = portStr.toIntOrNull() ?: return null
            }

            if (host.isEmpty()) return null
            if (port < 1 || port > 65535) return null

            // Use InetSocketAddress constructor which handles DNS resolution
            return InetSocketAddress(host, port)
        } catch (e: Exception) {
            return null
        }
    }
    /**
     * Format SocketAddress as string, handling both IPv4 and IPv6.
     * Omits port if it matches the default port.
     */
    companion object {
        fun formatAddress(address: SocketAddress, defaultPort: Int): String {
            return when (address) {
                is InetSocketAddress -> {
                    val host = InetAddresses.toAddrString(address.address)
                    val port = address.port

                    if (port == defaultPort) {
                        /* omit the port if it's the default port */
                        host
                    } else if (host.contains(':')) {
                        /* add brackets for IPv6 addresses if they contain colons */
                        "[$host]:$port"
                    } else {
                        "$host:$port"
                    }
                }

                else -> address.toString()
            }
        }
    }
}
