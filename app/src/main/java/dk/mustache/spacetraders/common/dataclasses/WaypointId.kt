package dk.mustache.spacetraders.common.dataclasses

data class WaypointId(val sectorId: String, val systemId: String, val waypointId: String) {
    companion object {
        fun fromString(waypointId: String): WaypointId {
            val parts = waypointId.split("-")
            return try {
                WaypointId(parts[0], parts.subList(0, 2).joinToString("-"), waypointId)
            } catch (e: Exception) {
                WaypointId("", "", "")
            }
        }
    }
}