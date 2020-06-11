package com.example.mypets

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 */
@Entity(tableName = "pets_table")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var pid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "breed") var breed: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "weight") val weight: Int?
)