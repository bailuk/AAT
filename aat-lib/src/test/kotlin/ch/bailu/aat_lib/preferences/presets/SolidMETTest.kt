package ch.bailu.aat_lib.preferences.presets

import ch.bailu.aat_lib.preferences.mock.MockStorage
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class SolidMETTest {

    @Test
    fun getMetValue() {
        val mockStorage = MockStorage()
        val solidMET = SolidMET(mockStorage, 0)

        mockStorage.mockStringValue = " 6.0 hiking, cross country"
        assertEquals(6.0f, solidMET.metValue)

        mockStorage.mockStringValue = "6.8 Radfahren, mittlere Leistung"
        assertEquals(6.8f, solidMET.metValue)

        mockStorage.mockStringValue = "2.0 Test"
        assertEquals(2.0f, solidMET.metValue)

        mockStorage.mockStringValue = "6 Radfahren, mittlere Leistung"
        assertEquals(6f, solidMET.metValue)

        mockStorage.mockStringValue = "6Radfahren, mittlere Leistung"
        assertEquals(0f, solidMET.metValue)

        mockStorage.mockStringValue = "Radfahren, mittlere Leistung"
        assertEquals(0f, solidMET.metValue)

    }
}
