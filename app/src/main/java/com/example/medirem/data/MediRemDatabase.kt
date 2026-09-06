package com.example.medirem.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Medicine::class], version = 1, exportSchema = false)
abstract class MediRemDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var INSTANCE: MediRemDatabase? = null

        fun getDatabase(context: Context): MediRemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediRemDatabase::class.java,
                    "medirem_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}