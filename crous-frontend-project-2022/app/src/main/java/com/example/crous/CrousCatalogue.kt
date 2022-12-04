package com.example.crous

class CrousCatalogue {
    private val crousMap: HashMap<String, ReducedCrous> = HashMap()
    private val crousMapFavorite: HashMap<String, ReducedCrous> = HashMap()

    fun addCrous(crous: ReducedCrous) {
        crousMap[crous.title] = crous
    }

    fun getCrousFromTitle(title: String):ReducedCrous {
        return crousMap[title] ?: throw java.lang.RuntimeException("No crous with title: $title")
    }

    fun findCrousById(Id: String): ReducedCrous? {
        val key= crousMap.filterValues { it.id == Id }.keys
        return crousMap.getValue(key.toString())
    }

    fun getAllCrous(): List<ReducedCrous> {
        return crousMap.values.sortedBy { it.address }
    }

    /*
    fun getCrousFromType(type: String): List<`reduced-crous`> { // this fuction can return all the crous coming from the type or the zone
        return crousMap.filterValues { it.type.equals(type, ignoreCase = true) }
            .values
            .sortedBy { it.address }
            .toList()
    }
     */
    fun toggleFavorite(Id: String){
        if (crousMapFavorite[Id] != null) {
            crousMapFavorite.remove(Id)
        }
        else {
            val key = crousMap.filterValues { it.id == Id }.keys
            crousMapFavorite[Id] = crousMap.getValue(key.toString())
        }
    }

    fun getTotalNumberOfCrous(): Int {
        return crousMap.size
    }

    fun clean() {
        crousMap.clear()
    }
}