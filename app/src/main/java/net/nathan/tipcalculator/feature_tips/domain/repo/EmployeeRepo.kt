package net.nathan.tipcalculator.feature_tips.domain.repo

import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

interface EmployeeRepo {
    suspend fun getAllEmployeeEntries(): List<EmployeeEntryItem>
    suspend fun getSingleEmployeeEntryById(id: Int): EmployeeEntryItem?
    suspend fun addEmployeeEntry(employee: EmployeeEntryItem)
    suspend fun updateEmployeeEntry(employee: EmployeeEntryItem)
    suspend fun deleteEmployeeEntry(employee: EmployeeEntryItem)
    suspend fun deleteAllEmployeeEntries()
    suspend fun getAllSelectedEmployeeEntries(): List<EmployeeEntryItem>
    suspend fun updateAllEmployeeEntries(employees: List<EmployeeEntryItem>)
}