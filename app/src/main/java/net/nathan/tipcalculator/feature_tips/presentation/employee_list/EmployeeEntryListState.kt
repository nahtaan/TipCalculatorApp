package net.nathan.tipcalculator.feature_tips.presentation.employee_list

import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

data class EmployeeEntryListState(
    val employeeEntryItems: List<EmployeeEntryItem> = emptyList(),
    val allSelected: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)