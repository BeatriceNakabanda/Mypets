package com.example.mypets

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: PetRepository
    // Using LiveData and caching what getPets returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPets: LiveData<List<Pet>>

    init {
        val petsDao = PetRoomDatabase.getDatabase(application, viewModelScope).petDao()
        repository = PetRepository(petsDao)
        allPets = repository.allPets
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(pet: Pet) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(pet)
    }
//    fun update(id: Int, updatedPet: Pet) = viewModelScope.launch(Dispatchers.IO) {
//        repository.update(id)
//    }
    fun update(pet: Pet) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(pet)
    }
    fun deletePet(pet: Pet) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePet(pet)
    }

}