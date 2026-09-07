package com.example.medirem.data

import androidx.room.*

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicines ORDER BY date DESC, time ASC")
    suspend fun getAllMedicines(): List<Medicine>

    @Query("SELECT * FROM medicines WHERE date = :date ORDER BY time ASC")
    suspend fun getMedicinesByDate(date: String): List<Medicine>

    @Insert
    suspend fun insertMedicine(medicine: Medicine): Long

    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicines WHERE id = :id LIMIT 1")
    suspend fun getMedicine(id: Int): Medicine?

    @Query("UPDATE medicines SET taken = :taken WHERE id = :id")
    suspend fun updateTakenStatus(id: Int, taken: Boolean)
}