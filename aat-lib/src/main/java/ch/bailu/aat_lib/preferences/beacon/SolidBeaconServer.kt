package ch.bailu.aat_lib.preferences.beacon

import ch.bailu.aat_lib.preferences.SolidSocketAddress
import ch.bailu.aat_lib.preferences.StorageInterface
import java.net.InetSocketAddress
import java.net.SocketAddress

class SolidBeaconServer(storage: StorageInterface) : SolidSocketAddress(storage, KEY, DEFAULT_PORT) {
    override fun getLabel(): String {
        return "Server";
    }

    override fun getDefaultValue(): SocketAddress? {
        return InetSocketAddress("127.0.0.1", DEFAULT_PORT)
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        getDefaultValue()?.let { defaultAddress ->
            list.add(formatAddress(defaultAddress, DEFAULT_PORT))
            list.add(formatAddress(InetSocketAddress("138.201.185.127", DEFAULT_PORT), DEFAULT_PORT))
        }
        return list
    }

    companion object {
        const val KEY = "BEACON_SERVER"

        const val DEFAULT_PORT = 5598
    }
}
