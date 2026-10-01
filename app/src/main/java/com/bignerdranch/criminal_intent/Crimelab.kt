package com.bignerdranch.criminal_intent

import java.util.UUID

object CrimeLab {
    private val crimes = mutableListOf<Crime>()

    init {
        for (i in 1..5) {
            val c = Crime()
            c.title = "Crime #$i"
            c.isSolved = i % 2 == 0
            crimes.add(c)
        }
    }

    fun getCrimes(): List<Crime> = crimes
    fun getCrime(id: UUID): Crime? = crimes.find { it.id == id }
}