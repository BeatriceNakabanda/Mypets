package com.example.mypets

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Pet class
@Database(entities = [Pet::class], version = 5)
abstract class PetRoomDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao


    private class PetDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         */
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // If you want to keep the data through app restarts,
            // comment out the following line.
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    //populateDatabase(database.petDao())
                    var petDao = database.petDao()

                    // Delete all pets.
                    petDao.deleteAll()

                    //Insert pet
                    var pet1 = Pet(name = "Berry", breed = "Chihuahua")
                    petDao.insert(pet1)
                    var pet2 = Pet(name = "Cockie", breed = "German Shepherd")
                    petDao.insert(pet2)

                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PetRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): PetRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PetRoomDatabase::class.java,
                        "pet_database"
                    )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(PetDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}