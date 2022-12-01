package com.example.crous_frontend

class CrousCatalogue {
    private val crousMap: HashMap<String, Crous> = HashMap()

    fun addCrous (crous: Crous){
        crousMap[crous.title] = crous
    }
    fun getCrous(address: String):Crous { // this function can return the crous coming from address or title or id
        return crousMap[address] ?: throw java.lang.RuntimeException("No crous with address: $address")
    }
    fun getAllCrous() : List<Crous> {
        return crousMap.values.sortedBy { it.address }
    }
    fun getCrousFrom(latitude: Number, longitude: Number): Crous {
        var k : String =""
        k = crousMap.keys.find {
            crousMap[it]?.latitude?.equals(latitude) == true && crousMap[it]?.longitude?.equals(longitude) == true }
            .toString()
        return crousMap[k] ?: throw java.lang.RuntimeException("No crous with latitude: $latitude and longitude: $longitude")
    }
    fun getCrousOf(type: String): List<Crous> { // this fuction can return all the crous coming from the type or the zone
        return crousMap.filterValues { it.type.equals(type, ignoreCase = true) }
            .values
            .sortedBy { it.address }
            .toList()
    }
    fun getTotalNumberOfCrous(): Int {
        return crousMap.size
    }
    fun clean() {
        crousMap.clear()
    }
}