package com.example.mypets

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PetDao{
    @Query("SELECT * FROM pets_table")
    fun getAll(): LiveData<List<Pet>>

//    @Query("SELECT * FROM pets_table WHERE uid IN (:petIds)")
//    fun loadAllByIds(petIds: IntArray): LiveData<List<Pet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg pets: Pet)

    @Delete
    fun deletePet(pet: Pet)

    @Delete
    fun deleteAll(vararg pets: Pet)

    @Update
    fun updatePet(pets: Pet)

}