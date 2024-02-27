package net.nathan.tipcalculator.feature_tips.presentation.employee_list

import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

sealed class EmployeeEntryListEvent {
    data class Delete(val employee: EmployeeEntryItem): EmployeeEntryListEvent()
    data class ToggleSelected(val employee: EmployeeEntryItem): EmployeeEntryListEvent()
    data class UndoDelete(val employee: EmployeeEntryItem) :  EmployeeEntryListEvent()
    data object ToggleAllSelected : EmployeeEntryListEvent()
    data object DeleteAll : EmployeeEntryListEvent()
}