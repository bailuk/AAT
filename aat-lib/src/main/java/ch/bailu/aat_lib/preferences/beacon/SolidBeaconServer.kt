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
        return InetSocketAddress("138.201.185.127", DEFAULT_PORT)
    }

    companion object {
        const val KEY = "BEACON_SERVER"

        const val DEFAULT_PORT = 5598
    }
}
