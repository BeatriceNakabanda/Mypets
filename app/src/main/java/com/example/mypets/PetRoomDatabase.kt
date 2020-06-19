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
@Database(entities = [Pet::class], version = 14)
abstract class PetRoomDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao


    private class PetDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        //Called when database is created for the first time after tables have been created
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    //populateDatabase(database.petDao())
                    val petDao = database.petDao()

                    //Insert pet
                    val pet1 = Pet(name = "Berry", breed = "Chihuahua", weight = 25, gender = 1)
                    petDao.insert(pet1)
                    val pet2 = Pet(name = "Cockie", breed = "German Shepherd", weight = 10, gender = 2)
                    petDao.insert(pet2)

                }
            }
        }

        //Called when the database was open
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val petDao = database.petDao()
                    // Delete all pets.
                    petDao.deleteAll()
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