package ch.bailu.aat_lib.service.elevation.updater

import ch.bailu.aat_lib.coordinates.Dem3Coordinates

class PendingUpdatesMap {
    private val map = HashMap<Dem3Coordinates, ArrayList<ElevationUpdaterClient>>()

    fun add(coordinates: Dem3Coordinates, elevationUpdater: ElevationUpdaterClient) {
        var elevationUpdaterClients = map[coordinates]

        if (elevationUpdaterClients == null) {
            elevationUpdaterClients = ArrayList(10)
            map[coordinates] = elevationUpdaterClients
        }

        if (!elevationUpdaterClients.contains(elevationUpdater)) {
            elevationUpdaterClients.add(elevationUpdater)
        }
    }

    fun remove(coordinates: Dem3Coordinates) {
        map.remove(coordinates)
    }


    private fun removeIfEmpty(coordinates: Dem3Coordinates) {
        val elevationUpdaterClients = map[coordinates]

        if (elevationUpdaterClients.isNullOrEmpty()) {
            remove(coordinates)
        }
    }

    fun remove(elevationUpdaterClient: ElevationUpdaterClient) {
        for (elevationUpdaterClients in map.values) {
            elevationUpdaterClients?.remove(elevationUpdaterClient)
        }
        removeAllEmpty()
    }

    private fun removeAllEmpty() {
        val coordinatesIterator = coordinates()
        while (coordinatesIterator.hasNext()) {
            removeIfEmpty(coordinatesIterator.next())
        }
    }

    fun coordinates(): Iterator<Dem3Coordinates> {
        return HashSet(map.keys).iterator()
    }

    fun get(coordinates: Dem3Coordinates): ArrayList<ElevationUpdaterClient>? {
        return map[coordinates]
    }
}
