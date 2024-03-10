package net.nathan.tipcalculator.feature_tips.presentation.employee_list

import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

sealed class EmployeeEntryListEvent {
    data class Delete(val employee: EmployeeEntryItem): EmployeeEntryListEvent()
    data class ToggleSelected(val employee: EmployeeEntryItem): EmployeeEntryListEvent()
    data class UndoDelete(val employee: EmployeeEntryItem) :  EmployeeEntryListEvent()
    data class ImportEmployeeStringChange(val value: String) : EmployeeEntryListEvent()
    data object ToggleAllSelected : EmployeeEntryListEvent()
    data object DeleteAll : EmployeeEntryListEvent()
    data object ImportEmployees : EmployeeEntryListEvent()
    data object ShowImportDialog : EmployeeEntryListEvent()
    data object HideImportDialog : EmployeeEntryListEvent()
}