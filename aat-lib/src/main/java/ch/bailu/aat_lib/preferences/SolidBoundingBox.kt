package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration

class SolidBoundingBox(storage: StorageInterface, key: String, private val label: String) :
    SolidTypeInterface {
    private val N = SolidInteger(storage, key + "_N")
    private val W = SolidInteger(storage, key + "_W")
    private val S = SolidInteger(storage, key + "_S")
    private val E = SolidInteger(storage, key + "_E")

    var value: BoundingBoxE6
        get() = BoundingBoxE6(
            N.getValue(),
            E.getValue(),
            S.getValue(),
            W.getValue()
        )
        set(b) {
            N.setValue(b.latNorthE6)
            E.setValue(b.lonEastE6)
            S.setValue(b.latSouthE6)
            W.setValue(b.lonWestE6)
        }

    override fun hasKey(key: String): Boolean {
        return N.hasKey(key) || E.hasKey(key) || S.hasKey(key) || W.hasKey(key)
    }

    override fun register(listener: OnPreferencesChanged) {
        N.register(listener)
    }

    override fun unregister(listener: OnPreferencesChanged) {
        N.unregister(listener)
    }

    override fun getValueAsString(): String {
        return value.toString()
    }

    override fun getKey(): String {
        return N.getKey().substring(0, N.getKey().length - 3)
    }

    override fun getStorage(): StorageInterface {
        return N.getStorage()
    }

    override fun getLabel(): String {
        return label
    }

    fun createSelectionStringOverlaps(): String {
        val n = N.getValue()
        val e = E.getValue()
        val s = S.getValue()
        val w = W.getValue()
        return ("(("
                + GpxDbConfiguration.KEY_NORTH_BOUNDING + " < " + n
                + " AND " + GpxDbConfiguration.KEY_NORTH_BOUNDING + " > " + s
                + ") OR ("
                + GpxDbConfiguration.KEY_SOUTH_BOUNDING + " < " + n
                + " AND " + GpxDbConfiguration.KEY_SOUTH_BOUNDING + " > " + s
                + "))"
                + " AND "
                + "(("
                + " AND " + GpxDbConfiguration.KEY_EAST_BOUNDING + " > " + w
                + " AND " + GpxDbConfiguration.KEY_EAST_BOUNDING + " < " + e
                + ") OR ("
                + " AND " + GpxDbConfiguration.KEY_WEST_BOUNDING + " > " + w
                + " AND " + GpxDbConfiguration.KEY_WEST_BOUNDING + " < " + e
                + "))")
    }

    fun createSelectionStringInside(): String {
        val n = N.getValue()
        val e = E.getValue()
        val s = S.getValue()
        val w = W.getValue()
        return GpxDbConfiguration.KEY_NORTH_BOUNDING + " < " + n +
                " AND " + GpxDbConfiguration.KEY_SOUTH_BOUNDING + " > " + s +
                " AND " + GpxDbConfiguration.KEY_EAST_BOUNDING + " < " + e +
                " AND " + GpxDbConfiguration.KEY_WEST_BOUNDING + " > " + w
    }

    override fun getToolTip(): String? {
        return null
    }
}
