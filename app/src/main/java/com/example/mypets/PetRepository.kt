package com.example.mypets

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class PetRepository(private val petDao: PetDao){
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //list of pets is initialized by getting live data from room
    val allPets: LiveData<List<Pet>> = petDao.getAll()

    //Suspend modifier tell the compiler insert() needs to be called
    // from another suspending function
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pet: Pet) {
        petDao.insert(pet)
    }
}