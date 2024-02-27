package net.nathan.tipcalculator.feature_tips.data.local

import androidx.room.*
import net.nathan.tipcalculator.feature_tips.data.local.dto.LocalEmployeeEntryItem

@Dao
interface EmployeeEntryDao {

    @Query("SELECT * FROM employees")
    suspend fun getAllEmployeeEntryItems(): List<LocalEmployeeEntryItem>

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getSingleEmployeeEntryById(id: Int): LocalEmployeeEntryItem?

    @Query("SELECT * FROM employees WHERE isSelected = true")
    suspend fun getAllSelectedEmployeeEntries(): List<LocalEmployeeEntryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllEmployeeEntries(entries: List<LocalEmployeeEntryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmployeeEntry(entry: LocalEmployeeEntryItem)

    @Update
    suspend fun updateEmployeeEntry(entry: LocalEmployeeEntryItem)

    @Transaction
    suspend fun updateAllEmployeeEntries(employees: List<LocalEmployeeEntryItem>){
        employees.forEach{
            updateEmployeeEntry(it)
        }
    }

    @Delete
    suspend fun deleteEmployeeEntry(entry: LocalEmployeeEntryItem)

    @Query("DELETE FROM employees WHERE 1 = 1")
    suspend fun deleteAllEmployeeEntries()
}