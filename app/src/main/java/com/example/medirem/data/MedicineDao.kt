package com.example.medirem.data

import androidx.room.*

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicines ORDER BY time ASC")
    suspend fun getAllMedicines(): List<Medicine>

    @Insert
    suspend fun insertMedicine(medicine: Medicine)

    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicines WHERE id = :id LIMIT 1")
    suspend fun getMedicine(id: Int): Medicine?

    @Query("UPDATE medicines SET taken = :taken WHERE id = :id")
    suspend fun updateTakenStatus(id: Int, taken: Boolean)
}