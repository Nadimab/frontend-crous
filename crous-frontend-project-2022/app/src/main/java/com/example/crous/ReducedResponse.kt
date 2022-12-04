package com.example.crous

data class ReducedResponse (
    val current : Int,
    val next : Int,
    val last : Int,
    val first : Int,
    val rows : Int,
    val returnData : ArrayList <ReducedCrous>
): java.io.Serializable
