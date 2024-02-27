package net.nathan.tipcalculator.feature_tips.domain.use_case

import net.nathan.tipcalculator.core.util.TipUseCaseStrings
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem
import net.nathan.tipcalculator.feature_tips.domain.repo.EmployeeRepo
import net.nathan.tipcalculator.feature_tips.domain.util.InvalidEmployeeItemException
import javax.inject.Inject

class TipUseCases @Inject constructor(
    private val repo: EmployeeRepo
) {
    suspend fun addEmployeeEntry(employee: EmployeeEntryItem){
        if(employee.name.isBlank()){
            throw InvalidEmployeeItemException(TipUseCaseStrings.INVALID_NAME)
        }
        repo.addEmployeeEntry(employee)
    }
    suspend fun resetSelectedTimes(){
        val selected = getAllSelectedEmployeeEntries()
        val updated = mutableListOf<EmployeeEntryItem>()
        for(employee in selected){
            updated.add(employee.copy(
                hours = 0,
                minutes = 0
            ))
        }
        repo.updateAllEmployeeEntries(updated)
    }
    suspend fun updateEmployeeEntry(employee: EmployeeEntryItem){
        if(employee.name.isBlank()){
            throw InvalidEmployeeItemException(TipUseCaseStrings.INVALID_NAME)
        }
        repo.addEmployeeEntry(employee)
    }
    suspend fun toggleEmployeeSelected(employee: EmployeeEntryItem){
        repo.updateEmployeeEntry(employee.copy(
            isSelected = !employee.isSelected
        ))
    }
    suspend fun getAllSelectedEmployeeEntries(): List<EmployeeEntryItem>{
        return repo.getAllSelectedEmployeeEntries()
    }
    suspend fun deleteEmployeeEntry(employee: EmployeeEntryItem){
        repo.deleteEmployeeEntry(employee)
    }
    suspend fun getEmployeeEntryById(id: Int): EmployeeEntryItem?{
        return repo.getSingleEmployeeEntryById(id)
    }
    suspend fun getAllEmployeeEntries(): List<EmployeeEntryItem>{
        return repo.getAllEmployeeEntries()
    }
    suspend fun deleteAllEmployeeEntries(){
        repo.deleteAllEmployeeEntries()
    }
}